package raytracer

import math.ray.Sphere
import math.vector.Vector3

class HitPayload {
    var distance = Double.POSITIVE_INFINITY
    var position = Vector3()
    var normal = Vector3()
    var sphere: Sphere? = null
}
