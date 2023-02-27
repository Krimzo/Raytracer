package raytracer

import entity.Entity
import math.triangle.Triangle
import math.triangle.Vertex
import math.vector.Vector3

class HitPayload {
    var hitDistance = Double.POSITIVE_INFINITY
    var hitPosition = Vector3()

    var hitTriangle: Triangle? = null
    var hitEntity: Entity? = null

    var interpolatedVertex = Vertex()
    var pixelColor = Vector3()

    fun getOffsetPosition(offset: Double = 1e-3): Vector3 {
        return (hitPosition + (interpolatedVertex.normal * offset))
    }
}
