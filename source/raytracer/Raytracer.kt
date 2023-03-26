package raytracer

import editor.EditorWindow
import entity.Entity
import math.*
import math.matrix.Matrix4x4
import math.ray.Ray
import math.triangle.Triangle
import math.vector.Vector2
import math.vector.Vector3
import math.vector.Vector4
import scene.Scene
import java.awt.Graphics

class Raytracer(private val editor: EditorWindow) {
    var squareSize = 75
    var bounceLimit = 6

    var sampleCenter = true
    var sampleCount = 8

    var scene = Scene()

    fun render() {
        val cameraMatrix = renderSetup()
        renderWireframe(cameraMatrix)
        renderTraced(cameraMatrix)
    }

    // Utility
    private fun renderSetup(): Matrix4x4 {
        editor.renderPanel.reallocateBuffer()
        scene.camera.aspect = editor.renderPanel.aspect
        scene.values.parallelStream().forEach(Entity::transformMesh)
        return scene.camera.matrix()
    }

    private fun getNDC(x: Double, y: Double): Vector2 {
        val width = editor.renderPanel.buffer.width
        val height = editor.renderPanel.buffer.height
        val y = (height - 1.0 - y)

        val result = Vector2()
        result.x = (x / (width - 1)) * 2 - 1
        result.y = (y / (height - 1)) * 2 - 1
        return result
    }

    private fun fromNDC(x: Double, y: Double): Vector2 {
        val width = editor.renderPanel.buffer.width
        val height = editor.renderPanel.buffer.height
        val y = -y

        val result = Vector2()
        result.x = ((x + 1) * 0.5) * (width - 1)
        result.y = ((y + 1) * 0.5) * (height - 1)
        return result
    }

    // Wireframe
    private fun renderWireframe(cameraMatrix: Matrix4x4) {
        // Setup
        editor.renderPanel.clear(scene.camera.background.color)

        // Render
        scene.values.parallelStream().forEach { entity ->
            editor.renderPanel.buffer.graphics.let {
                it.color = entity.material?.color?.color
                entity.renderMesh.parallelStream().forEach { triangle ->
                    renderWireframeTriangle(it, cameraMatrix, triangle)
                }
                it.dispose()
            }
        }

        // Finalize
        editor.renderPanel.repaint()
    }

    private fun renderWireframeTriangle(graphics: Graphics, cameraMatrix: Matrix4x4, triangle: Triangle) {
        var a = (cameraMatrix * Vector4(triangle.a.world, 1.0)); a /= a.w
        var b = (cameraMatrix * Vector4(triangle.b.world, 1.0)); b /= b.w
        var c = (cameraMatrix * Vector4(triangle.c.world, 1.0)); c /= c.w

        if (!isUnit(a.z) || !isUnit(b.z) || !isUnit(c.z)) {
            return
        }

        val aCoords = fromNDC(a.x, a.y)
        val bCoords = fromNDC(b.x, b.y)
        val cCoords = fromNDC(c.x, c.y)

        graphics.drawLine(aCoords.x.toInt(), aCoords.y.toInt(), bCoords.x.toInt(), bCoords.y.toInt())
        graphics.drawLine(bCoords.x.toInt(), bCoords.y.toInt(), cCoords.x.toInt(), cCoords.y.toInt())
        graphics.drawLine(cCoords.x.toInt(), cCoords.y.toInt(), aCoords.x.toInt(), aCoords.y.toInt())
    }

    // Tracing
    private fun renderTraced(cameraMatrix: Matrix4x4) {
        // Setup
        val inverseCameraMatrix = inverse(cameraMatrix)
        val jobs = JobQueue()

        // Render
        for (square in getRenderSquares(editor.renderPanel.width, editor.renderPanel.height)) {
            jobs.addJob { id ->
                editor.renderPanel.squares[id] = square
                renderSquare(square, inverseCameraMatrix)
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
                    renderPixel(x, y, inverseCamera)
                }
            }
            editor.repaint()
        }
    }

    private fun renderPixel(x: Int, y: Int, inverseCamera: Matrix4x4) {
        var pixelColor = Vector3()

        // Sample center
        if (sampleCenter) {
            val ndc = getNDC(x.toDouble(), y.toDouble())
            val ray = Ray(scene.camera.position, inverseCamera, ndc)
            pixelColor += traceRay(ray, 0, null).pixelColor
        }

        // Sample circular
        for (i in 0 until sampleCount) {
            var samplePosition = Vector2(0.0, 0.75)
            samplePosition = rotate(samplePosition, i * (360.0 / sampleCount))
            samplePosition += Vector2(x.toDouble(), y.toDouble())

            val ndc = getNDC(samplePosition.x, samplePosition.y)
            val ray = Ray(scene.camera.position, inverseCamera, ndc)
            pixelColor += traceRay(ray, 0, null).pixelColor
        }

        // Average color
        var sampleCount = sampleCount.toDouble()
        if (sampleCenter) { sampleCount += 1.0 }
        pixelColor /= sampleCount

        // Write to buffer
        editor.renderPanel.buffer.setPixel(x, y, pixelColor.color)
    }

    private fun traceRay(ray: Ray, bounceIndex: Int, sourceTriangle: Triangle?): HitPayload {
        val payload = HitPayload()
        val tempPosition = Vector3()

        // Entity loop
        for (entity in scene) {
            if (!entity.value.canBeHit(ray)) {
                continue
            }

            for (triangle in entity.value.renderMesh) {
                if (triangle === sourceTriangle || !ray.intersect(triangle, tempPosition)) {
                    continue
                }

                val intersectionDistance = (tempPosition - ray.origin).length
                if (intersectionDistance >= payload.hitDistance) {
                    continue
                }

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

        if (bounceIndex < bounceLimit && roughness < 1) {
            val rayOrigin = payload.hitPosition
            val rayDirection = reflect(ray.direction, payload.interpolatedVertex.normal)

            val bouncePayload = traceRay(Ray(rayOrigin, rayDirection), bounceIndex + 1, hitPayload.hitTriangle)
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
        val shadowRayOrigin = payload.hitPosition
        val shadowRayDirection = -scene.selectedDirectionalLight.direction
        val shadowFactor = getShadowFactor(Ray(shadowRayOrigin, shadowRayDirection), payload.hitTriangle)

        // Light color
        val roughness = payload.hitEntity?.material?.roughness ?: 1.0
        val diffuseColor = scene.selectedDirectionalLight.getFull(payload.interpolatedVertex.normal)
        val totalLight = (diffuseColor * shadowFactor * roughness)

        // Material color
        var entityColor = Vector3()
        payload.hitEntity?.material?.let {
            entityColor = it.computeColor(payload.interpolatedVertex.texture)
        }

        payload.pixelColor = Vector3(
            entityColor.x * totalLight.x,
            entityColor.y * totalLight.y,
            entityColor.z * totalLight.z,
        )
        return payload
    }

    private fun getShadowFactor(ray: Ray, sourceTriangle: Triangle?): Double {
        for (entity in scene) {
            if (!entity.value.canBeHit(ray)) {
                continue
            }

            for (triangle in entity.value.renderMesh) {
                if (triangle !== sourceTriangle && ray.intersect(triangle)) {
                    return 0.0
                }
            }
        }
        return 1.0
    }
}
