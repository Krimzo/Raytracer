package raytracer

import entity.Entity
import math.triangle.Triangle
import math.triangle.Vertex
import math.vector.Float3

class HitPayload {
    var hitDistance = Float.POSITIVE_INFINITY
    var hitPosition = Float3()

    var hitTriangle: Triangle? = null
    var hitEntity: Entity? = null

    var interpolatedVertex = Vertex()
    var pixelColor = Float3()
}
