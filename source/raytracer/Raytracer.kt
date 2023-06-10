package raytracer

import math.inverse
import math.matrix.Matrix4x4
import math.normalize
import math.ray.Ray
import math.vector.Vector3
import math.vector.Vector4
import scene.Scene
import utility.Random
import java.awt.Dimension
import java.util.stream.IntStream

class Raytracer {
    private var frameBuffer = FrameBuffer(Dimension(1, 1))
    private val bounceLimit = 6

    fun reset() {
        frameBuffer = FrameBuffer(frameBuffer.size)
    }

    fun resize(dimension: Dimension) {
        if (dimension.width != frameBuffer.width || dimension.height != frameBuffer.height) {
            frameBuffer = FrameBuffer(dimension)
        }
    }

    fun save(path: String) {
        frameBuffer.saveAsPNG(path)
    }

    fun render(scene: Scene): FrameBuffer {
        frameBuffer.newFrame()
        val inverseCamera = inverse(scene.camera.matrix())
        scene.camera.aspect = frameBuffer.width.toDouble() / frameBuffer.height
        IntStream.range(0, frameBuffer.width * frameBuffer.height).parallel().forEach { i ->
            val x = i % frameBuffer.width; val y = i / frameBuffer.width
            val light = perPixel(scene, x, y, inverseCamera)
            frameBuffer.addLight(i, light)
        }
        return frameBuffer
    }

    private fun perPixel(scene: Scene, x: Int, y: Int, inverseCamera: Matrix4x4): Vector3 {
        val ndc = frameBuffer.getNDC(x.toDouble(), y.toDouble())
        val ray = Ray(scene.camera.position, inverseCamera, ndc)

        var light = Vector3(0.0)
        var contribution = Vector3(1.0)

        for (i in 0 until bounceLimit) {
            val payload = traceRay(scene, ray)
            if (payload.distance < 0) {
                break
            }

            val material = payload.sphere?.material ?: continue
            contribution = contribution multiply material.albedo
            light += material.totalEmission()
            light = light multiply contribution

            ray.origin = payload.position + payload.normal * 0.0001
            ray.direction = normalize(payload.normal + Random.unitSphere())
        }
        return light
    }

    private fun traceRay(scene: Scene, ray: Ray): HitPayload {
        val payload = HitPayload()
        for (sphere in scene) {
            val intersectData = Vector4()
            if (ray.intersectSphere(sphere, intersectData) && intersectData.w < payload.distance) {
                payload.distance = intersectData.w
                payload.sphere = sphere
            }
        }
        if (payload.sphere == null) {
            return onMiss(payload)
        }
        return onHit(payload, ray)
    }

    private fun onMiss(payload: HitPayload): HitPayload {
        payload.distance = -1.0
        return payload
    }

    private fun onHit(payload: HitPayload, ray: Ray): HitPayload {
        val sphere = payload.sphere ?: return payload
        val origin = ray.origin - sphere.origin
        payload.position = origin + ray.direction * payload.distance
        payload.normal = normalize(payload.position)
        payload.position += sphere.origin
        return payload
    }
}
