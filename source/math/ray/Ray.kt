package math.ray

import math.inverse
import math.normalize
import math.triangle.Triangle
import math.vector.Vector2
import math.vector.Vector3
import math.vector.Vector4
import scene.Camera
import java.io.Serializable
import kotlin.math.abs
import kotlin.math.sqrt

class Ray : Serializable {
    var origin: Vector3 = Vector3()
    var direction: Vector3 = Vector3()
        set(direction) { field = normalize(direction) }

    constructor()

    constructor(origin: Vector3, direction: Vector3) {
        this.origin = origin
        this.direction = direction
    }

    constructor(camera: Camera, ndc: Vector2) {
        origin = camera.position

        var pixelDirection = inverse(camera.matrix()) * Vector4(ndc.x, ndc.y, 1.0, 1.0)
        pixelDirection /= pixelDirection.w
        direction = pixelDirection.xyz
    }

    constructor(ray: Ray) {
        origin = Vector3(ray.origin)
        direction = Vector3(ray.direction)
    }

    fun intersect(plane: Plane, outIntersection: Vector3): Boolean {
        val denom = normalize(plane.normal) * direction
        if (abs(denom) > 0.0001) {
            val t = (plane.origin - origin) * plane.normal / denom
            if (t >= 0) {
                val res = origin + direction * t
                outIntersection.x = res.x
                outIntersection.y = res.y
                outIntersection.z = res.z
                return true
            }
        }
        return false
    }

    fun intersect(triangle: Triangle, outIntersection: Vector3): Boolean {
        val edge1 = (triangle.b.world - triangle.a.world)
        val edge2 = (triangle.c.world - triangle.a.world)

        val h = (direction x edge2)
        val s = (origin - triangle.a.world)
        val f = (1.0 / (edge1 * h))
        val u = (s * h * f)
        if (u < 0 || u > 1) {
            return false
        }

        val q = (s x edge1)
        val v = (direction * q * f)
        if (v < 0 || (u + v) > 1) {
            return false
        }

        val t = (edge2 * q * f)
        if (t > 0) {
            val res = (origin + direction * t)
            outIntersection.x = res.x
            outIntersection.y = res.y
            outIntersection.z = res.z
            return true
        }
        return false
    }

    fun intersectSphere(sphere: Sphere, outData: Vector4? = null): Boolean {
        val centerRay = (sphere.origin - origin)
        val cdDot = (centerRay * direction)
        if (cdDot < 0) {
            return false
        }

        val ccDot = (centerRay * centerRay - cdDot * cdDot)
        val rr = (sphere.radius * sphere.radius)
        if (ccDot > rr) {
            return false
        }

        val thc = sqrt(rr - ccDot)
        val dis0 = (cdDot - thc)
        val dis1 = (cdDot + thc)
        outData?.let {
            it.w = if (dis0 < 0) dis1 else dis0
            it.x = (origin.x + direction.x * it.w)
            it.y = (origin.y + direction.y * it.w)
            it.z = (origin.z + direction.z * it.w)
        }
        return true
    }

    fun intersectSphere(sphere: Sphere): Boolean {
        val rayLength = (sphere.origin - origin) * direction
        val rayPoint = (direction * rayLength + origin)
        val sphereRayDistance = (sphere.origin - rayPoint).length
        return sphereRayDistance <= sphere.radius
    }
}