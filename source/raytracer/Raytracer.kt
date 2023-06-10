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

    fun render(scene: Scene): FrameBuffer {
        frameBuffer.newFrame()
        renderTraced(scene)
        return frameBuffer
    }

    private fun renderTraced(scene: Scene) {
        val inverseCamera = inverse(scene.camera.matrix())
        scene.camera.aspect = frameBuffer.width.toDouble() / frameBuffer.height
        IntStream.range(0, frameBuffer.width * frameBuffer.height).parallel().forEach { i ->
            val x = i % frameBuffer.width
            val y = i / frameBuffer.width
            renderPixel(scene, x, y, inverseCamera)
        }
    }

    private fun renderPixel(scene: Scene, x: Int, y: Int, inverseCamera: Matrix4x4) {
        val ndc = frameBuffer.getNDC(x.toDouble(), y.toDouble())
        val ray = Ray(scene.camera.position, inverseCamera, ndc)
        val light = traceRay(scene, ray, 0).light
        frameBuffer.addLight(x, y, light)
    }

    private fun traceRay(scene: Scene, ray: Ray, bounceIndex: Int): HitPayload {
        var payload = HitPayload()
        for (sphere in scene) {
            val hitData = Vector4()
            if (!ray.intersectSphere(sphere, hitData)) {
                continue
            }
            if (hitData.w >= payload.distance) {
                continue
            }
            payload.distance = hitData.w
            payload.sphere = sphere
            payload.position = hitData.xyz
            payload.normal = normalize(payload.position - sphere.origin)
        }

        // Miss
        if (payload.sphere == null) {
            return onMiss(payload)
        }

        // Hit
        payload = onHit(payload)
        if (bounceIndex < bounceLimit) {
            val bounceRay = Ray()
            bounceRay.origin = payload.position + payload.normal * 0.0001
            bounceRay.direction = normalize(payload.normal + Random.unitSphere())
            val bouncePayload = traceRay(scene, bounceRay, bounceIndex + 1)
            payload.light += bouncePayload.light
        }
        return payload
    }

    private fun onMiss(payload: HitPayload): HitPayload {
        payload.light = Vector3(0.0)
        return payload
    }

    private fun onHit(payload: HitPayload): HitPayload {
        payload.sphere?.material?.let {
            payload.throughput = payload.throughput multiply it.albedo
            payload.light += it.totalEmission()// multiply payload.throughput
        }
        return payload
    }
}
