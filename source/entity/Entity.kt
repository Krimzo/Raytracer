package entity

import entity.material.Material
import entity.mesh.Mesh
import math.matrix.Float4x4
import math.ray.Ray
import math.ray.Sphere
import math.triangle.Triangle
import math.vector.Float2
import math.vector.Float3
import math.vector.Float4
import java.io.Serializable

class Entity : Serializable {
    var scale = Float3(1f)
    var rotation = Float3()
    var position = Float3()

    var mesh: Mesh? = null
    var transformedMesh = Mesh()

    var material: Material? = null

    fun transformMesh() {
        val matrix = matrix()
        transformedMesh = Mesh()

        mesh?.let {
            for (triangle in it) {
                val transformedTriangle = Triangle()

                // World
                transformedTriangle.a.world = (matrix * Float4(triangle.a.world, 1f)).xyz
                transformedTriangle.b.world = (matrix * Float4(triangle.b.world, 1f)).xyz
                transformedTriangle.c.world = (matrix * Float4(triangle.c.world, 1f)).xyz

                // Texture
                transformedTriangle.a.texture = Float2(triangle.a.texture)
                transformedTriangle.b.texture = Float2(triangle.b.texture)
                transformedTriangle.c.texture = Float2(triangle.c.texture)

                // Normal
                transformedTriangle.a.normal = (matrix * Float4(triangle.a.normal, 0f)).xyz
                transformedTriangle.b.normal = (matrix * Float4(triangle.b.normal, 0f)).xyz
                transformedTriangle.c.normal = (matrix * Float4(triangle.c.normal, 0f)).xyz

                transformedMesh.add(transformedTriangle)
            }
        }
    }

    fun canBeHit(ray: Ray): Boolean {
        val sphere = Sphere()
        sphere.origin = Float3(position)
        sphere.radius = mesh?.maxRadius ?: 0f
        return ray.intersectSphere(sphere)
    }

    private fun matrix(): Float4x4 {
        return Float4x4.translation(position) * Float4x4.rotation(rotation) * Float4x4.scaling(scale)
    }
}
