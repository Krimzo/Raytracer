package raytracer

import editor.EditorWindow
import math.inverse
import math.lerp
import math.matrix.Matrix4x4
import math.ray.Ray
import math.reflect
import math.rotate
import math.vector.Vector2
import math.vector.Vector3
import render.FrameBuffer
import scene.Scene

class Raytracer(private val editor: EditorWindow) {
    var squareSize = 75
    var bounceLimit = 6

    var sampleCenter = true
    var sampleCount = 8

    var scene = Scene()

    fun render() {
        // Setup
        editor.renderPanel.reallocateBuffer()
        scene.camera.aspect = editor.renderPanel.aspect
        val inverseCamera = inverse(scene.camera.matrix())
        scene.values.stream().parallel().forEach {
            it.transformMesh()
        }

        // Render
        val jobs = JobQueue()
        for (square in getRenderSquares(editor.renderPanel.width, editor.renderPanel.height)) {
            jobs.addJob {
                editor.renderPanel.squares[Thread.currentThread().id] = square
                renderSquare(square, inverseCamera)
            }
        }

        // Finalize
        jobs.finalize()
        editor.renderPanel.squares.clear()
        editor.renderPanel.repaint()
    }

    private fun getRenderSquares(width: Int, height: Int): ArrayList<Square> {
        val squares = ArrayList<Square>()
        for (y in 0 until (height / squareSize + 1)) {
            for (x in 0 until (width / squareSize + 1)) {
                val square = Square()
                square.x = (x * squareSize)
                square.y = (y * squareSize)
                square.size = squareSize
                squares.add(square)
            }
        }
        return squares
    }

    private fun renderSquare(square: Square, inverseCamera: Matrix4x4) {
        for (y in square.y until (square.y + square.size)) {
            for (x in square.x until (square.x + square.size)) {
                if (editor.renderPanel.buffer.isValidPosition(x, y)) {
                    renderPixel(x, y, editor.renderPanel.buffer, inverseCamera)
                }
            }
            editor.repaint()
        }
    }

    private fun renderPixel(x: Int, y: Int, buffer: FrameBuffer, inverseCamera: Matrix4x4) {
        var pixelColor = Vector3()

        // Sample center
        if (sampleCenter) {
            val ndc = getNDC(x.toDouble(), y.toDouble(), buffer.width, buffer.height)
            val ray = Ray(scene.camera.position, inverseCamera, ndc)
            pixelColor += traceRay(ray, 0).pixelColor
        }

        // Sample circular
        for (i in 0 until sampleCount) {
            var samplePosition = Vector2(0.0, 0.75)
            samplePosition = rotate(samplePosition, i * (360.0 / sampleCount))
            samplePosition += Vector2(x.toDouble(), y.toDouble())

            val ndc = getNDC(samplePosition.x, samplePosition.y, buffer.width, buffer.height)
            val ray = Ray(scene.camera.position, inverseCamera, ndc)
            pixelColor += traceRay(ray, 0).pixelColor
        }

        // Average color
        var sampleCount = sampleCount.toDouble()
        if (sampleCenter) { sampleCount += 1.0 }
        pixelColor /= sampleCount

        // Write to buffer
        buffer.setPixel(x, y, pixelColor.color)
    }

    private fun getNDC(x: Double, y: Double, width: Int, height: Int): Vector2 {
        Vector2(x / (width - 1.0), (height - 1.0 - y) / (height - 1.0)).let {
            return ((it * 2.0) - Vector2(1.0))
        }
    }

    private fun traceRay(ray: Ray, bounceIndex: Int): HitPayload {
        val payload = HitPayload()
        val tempPosition = Vector3()

        // Entity loop
        for (entity in scene) {
            if (!entity.value.canBeHit(ray)) { continue }
            for (triangle in entity.value.renderMesh) {
                if (!ray.intersect(triangle, tempPosition)) { continue }
                val intersectionDistance = (tempPosition - ray.origin).length

                if (intersectionDistance >= payload.hitDistance) { continue }
                payload.hitTriangle = triangle
                payload.hitEntity = entity.value
                payload.hitPosition = Vector3(tempPosition)
                payload.hitDistance = intersectionDistance
            }
        }

        // Miss
        if (payload.hitEntity == null) {
            return onMiss(payload)
        }

        // Hit
        val hitPayload = onHit(payload)
        val roughness = payload.hitEntity?.material?.roughness ?: 1.0

        if (bounceIndex < bounceLimit && roughness < 1.0) {
            val rayOrigin = payload.getOffsetPosition()
            val rayDirection = reflect(ray.direction, payload.interpolatedVertex.normal)

            val bouncePayload = traceRay(Ray(rayOrigin, rayDirection), bounceIndex + 1)
            hitPayload.pixelColor = lerp(bouncePayload.pixelColor, hitPayload.pixelColor, roughness)
        }
        return hitPayload
    }

    private fun onMiss(payload: HitPayload): HitPayload {
        payload.pixelColor = scene.camera.background
        return payload
    }

    private fun onHit(payload: HitPayload): HitPayload {
        // Interpolation
        payload.hitTriangle?.let {
            payload.interpolatedVertex = it.interpolate(payload.hitPosition)
        }

        // Shadow
        val shadowRayOrigin = payload.getOffsetPosition()
        val shadowRayDirection = -scene.directionalLight.direction
        val shadowFactor = getShadowFactor(Ray(shadowRayOrigin, shadowRayDirection))

        // Light color
        val roughness = payload.hitEntity?.material?.roughness ?: 1.0
        val diffuseColor = scene.directionalLight.getFull(payload.interpolatedVertex.normal)
        val totalLight = diffuseColor * shadowFactor * roughness

        // Material color
        var entityColor = Vector3()
        payload.hitEntity?.material?.let {
            entityColor = it.getColor(payload.interpolatedVertex.texture)
        }

        payload.pixelColor = Vector3(
            entityColor.x * totalLight.x,
            entityColor.y * totalLight.y,
            entityColor.z * totalLight.z,
        )
        return payload
    }

    private fun getShadowFactor(ray: Ray): Double {
        for (entity in scene) {
            if (!entity.value.canBeHit(ray)) { continue }
            for (triangle in entity.value.renderMesh) {
                if (ray.intersect(triangle)) {
                    return 0.0
                }
            }
        }
        return 1.0
    }
}
