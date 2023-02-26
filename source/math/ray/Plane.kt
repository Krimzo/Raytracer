package math.ray

import math.normalize
import math.vector.Vector3
import java.io.Serializable

class Plane : Serializable {
    var origin = Vector3()
    var normal = Vector3()
        set(normal) { field = normalize(normal) }

    constructor()

    constructor(normal: Vector3, point: Vector3) {
        this.normal = normal
        this.origin = point
    }

    constructor(plane: Plane) {
        normal = Vector3(plane.normal)
        origin = Vector3(plane.origin)
    }
}