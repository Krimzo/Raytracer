package raytracer

import math.ray.Ray
import math.vector.Float2
import math.vector.Float3
import math.vector.Int2
import scene.Scene
import window.FrameBuffer
import java.util.stream.IntStream

class Raytracer {
    var scene = Scene()

    fun render(buffer: FrameBuffer): Double {
        // Setup
        val startTime = System.nanoTime()
        scene.camera.aspect = buffer.width.toFloat() / buffer.height

        // Transform meshes
        for (entity in scene) {
            entity.value.transformMesh()
        }

        // Trace
        IntStream.range(0, buffer.height).parallel().forEach { y ->
            IntStream.range(0, buffer.width).parallel().forEach { x ->
                val ndc = getNDC(Int2(x, y), buffer.size)
                val pixelColor = perPixel(ndc)
                buffer.setPixel(Int2(x, y), pixelColor.color)
            }
        }
        return (System.nanoTime() - startTime) * 1e-6
    }

    private fun perPixel(ndc: Float2): Float3 {
        val ray = Ray(scene.camera, ndc)
        val payload = traceRay(ray)
        return payload.pixelColor
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

    private fun getNDC(position: Int2, frameSize: Int2): Float2 {
        var ndc = Float2(
            position.x / (frameSize.x - 1f),
            (frameSize.y - 1f - position.y) / (frameSize.y - 1f)
        )
        ndc *= 2f
        ndc -= Float2(1f)
        return ndc
    }
}
