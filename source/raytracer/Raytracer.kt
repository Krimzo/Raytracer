package raytracer

import math.ray.Ray
import math.vector.Float2
import math.vector.Float3
import scene.Scene
import window.FrameBuffer
import window.Window
import java.awt.Color

class Raytracer {
    private val squareSize = 100
    var scene = Scene()

    fun render(window: Window) {
        // Setup
        window.target.buffer.clear(Color.BLACK)
        scene.camera.aspect = window.width.toFloat() / window.height
        for (entity in scene) {
            entity.value.transformMesh()
        }

        // Render
        val jobs = JobQueue()
        for (square in getRenderSquares(window.width, window.height)) {
            jobs.addJob {
                window.target.squares[Thread.currentThread().id] = square
                renderSquare(square, window)
            }
        }

        // Finalize
        jobs.finalize()
        window.target.squares.clear()
        window.repaint()
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

    private fun renderSquare(square: Square, window: Window) {
        for (y in square.y until (square.y + square.size)) {
            for (x in square.x until (square.x + square.size)) {
                if (window.target.buffer.isValidPosition(x, y)) {
                    renderPixel(x, y, window.target.buffer)
                }
            }
            window.repaint()
        }
    }

    private fun renderPixel(x: Int, y: Int, buffer: FrameBuffer) {
        val ndc = getNDC(x, y, buffer.width, buffer.height)
        val ray = Ray(scene.camera, ndc)
        val pixelColor = traceRay(ray).pixelColor
        buffer.setPixel(x, y, pixelColor.color)
    }

    private fun getNDC(x: Int, y: Int, width: Int, height: Int): Float2 {
        Float2(x / (width - 1f), (height - 1f - y) / (height - 1f)).let {
            return ((it * 2f) - Float2(1f))
        }
    }

    private fun traceRay(ray: Ray): HitPayload {
        val payload = HitPayload()
        val tempPosition = Float3()

        // Entity loop
        for (entity in scene) {
            if (!entity.value.canBeHit(ray)) { continue }
            for (triangle in entity.value.transformedMesh) {
                if (!ray.intersect(triangle, tempPosition)) { continue }
                val intersectionDistance = (tempPosition - ray.origin).length

                if (intersectionDistance >= payload.hitDistance) { continue }
                payload.hitTriangle = triangle
                payload.hitEntity = entity.value
                payload.hitPosition = Float3(tempPosition)
                payload.hitDistance = intersectionDistance
            }
        }

        // Hit check
        if (payload.hitEntity == null) {
            return onMiss(payload)
        }
        return onHit(payload)
    }

    private fun onHit(payload: HitPayload): HitPayload {
        // Interpolation
        payload.hitTriangle?.let {
            payload.interpolatedVertex = it.interpolate(payload.hitPosition)
        }

        // Light color
        val ambientColor = scene.ambientLight.getFull()
        val diffuseColor = scene.directionalLight.getFull(payload.interpolatedVertex.normal)
        val totalLight = ambientColor + diffuseColor

        // Material color
        var entityColor = Float3()
        payload.hitEntity?.material?.let {
            entityColor = it.getColor(payload.interpolatedVertex.texture)
        }

        payload.pixelColor = Float3(
            entityColor.x * totalLight.x,
            entityColor.y * totalLight.y,
            entityColor.z * totalLight.z,
        )
        return payload
    }

    private fun onMiss(payload: HitPayload): HitPayload {
        payload.pixelColor = scene.camera.background
        return payload
    }
}
