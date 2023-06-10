package raytracer

import math.ray.Sphere
import math.vector.Vector3

class HitPayload {
    var distance = Double.POSITIVE_INFINITY
    var sphere: Sphere? = null

    var position = Vector3()
    var normal = Vector3()

    var throughput = Vector3(1.0)
    var light = Vector3()
}
