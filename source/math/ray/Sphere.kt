package math.ray

import math.vector.Vector3
import java.io.Serializable

class Sphere : Serializable {
    var origin: Vector3 = Vector3()
    var radius: Double = 0.0

    constructor()

    constructor(center: Vector3, radius: Double) {
        this.origin = center
        this.radius = radius
    }

    constructor(sphere: Sphere) {
        origin = Vector3(sphere.origin)
        radius = sphere.radius
    }
}