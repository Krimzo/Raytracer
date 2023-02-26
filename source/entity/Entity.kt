package entity

import entity.material.Material
import entity.mesh.RenderMesh
import entity.mesh.StorageMesh
import math.matrix.Matrix4x4
import math.ray.Ray
import math.ray.Sphere
import math.vector.Vector3
import java.io.Serializable

class Entity : Serializable {
    var scale = Vector3(1.0)
    var rotation = Vector3()
    var position = Vector3()

    var storageMesh: StorageMesh? = null
    var renderMesh = RenderMesh()

    var material: Material? = null

    fun transformMesh() {
        storageMesh?.let {
            val scaling = Matrix4x4.scaling(scale)
            val rotationTranslation = (Matrix4x4.translation(position) * Matrix4x4.rotation(rotation))
            renderMesh = RenderMesh(it, scaling, rotationTranslation)
        } ?: run {
            renderMesh = RenderMesh()
        }
    }

    fun canBeHit(ray: Ray): Boolean {
        val sphere = Sphere()
        sphere.origin = Vector3(position)
        sphere.radius = renderMesh.maxRadius
        return ray.intersectSphere(sphere)
    }
}
