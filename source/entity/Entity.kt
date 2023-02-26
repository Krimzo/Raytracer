package entity

import entity.material.Material
import entity.mesh.Mesh
import math.matrix.Matrix4x4
import math.ray.Ray
import math.ray.Sphere
import math.triangle.Triangle
import math.vector.Vector2
import math.vector.Vector3
import math.vector.Vector4
import java.io.Serializable

class Entity : Serializable {
    var scale = Vector3(1.0)
    var rotation = Vector3()
    var position = Vector3()

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
                transformedTriangle.a.world = (matrix * Vector4(triangle.a.world, 1.0)).xyz
                transformedTriangle.b.world = (matrix * Vector4(triangle.b.world, 1.0)).xyz
                transformedTriangle.c.world = (matrix * Vector4(triangle.c.world, 1.0)).xyz

                // Texture
                transformedTriangle.a.texture = Vector2(triangle.a.texture)
                transformedTriangle.b.texture = Vector2(triangle.b.texture)
                transformedTriangle.c.texture = Vector2(triangle.c.texture)

                // Normal
                transformedTriangle.a.normal = (matrix * Vector4(triangle.a.normal, 0.0)).xyz
                transformedTriangle.b.normal = (matrix * Vector4(triangle.b.normal, 0.0)).xyz
                transformedTriangle.c.normal = (matrix * Vector4(triangle.c.normal, 0.0)).xyz

                transformedMesh.add(transformedTriangle)
            }
        }
    }

    fun canBeHit(ray: Ray): Boolean {
        val sphere = Sphere()
        sphere.origin = Vector3(position)
        sphere.radius = mesh?.maxRadius ?: 0.0
        return ray.intersectSphere(sphere)
    }

    private fun matrix(): Matrix4x4 {
        return Matrix4x4.translation(position) * Matrix4x4.rotation(rotation) * Matrix4x4.scaling(scale)
    }
}
