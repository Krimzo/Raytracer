package math.ray

import math.vector.Float3
import java.io.Serializable

class Sphere : Serializable {
    var origin: Float3 = Float3()
    var radius: Float = 0f

    constructor()

    constructor(center: Float3, radius: Float) {
        this.origin = center
        this.radius = radius
    }

    constructor(sphere: Sphere) {
        origin = Float3(sphere.origin)
        radius = sphere.radius
    }
}