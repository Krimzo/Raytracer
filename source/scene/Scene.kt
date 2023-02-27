package scene

import entity.Entity
import entity.material.Material
import entity.material.Texture
import entity.mesh.StorageMesh
import logging.Logger
import scene.light.DirectionalLight
import java.io.Serializable

class Scene : LinkedHashMap<String, Entity>(), Serializable {
    val meshes = LinkedHashMap<String, StorageMesh>()
    val textures = LinkedHashMap<String, Texture>()
    val materials = LinkedHashMap<String, Material>()

    var directionalLight = DirectionalLight()

    var camera = Camera()

    init {
        Logger.log("Created empty scene")
    }
}
