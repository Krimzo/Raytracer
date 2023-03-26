package scene

import entity.Entity
import entity.material.Material
import entity.material.Texture
import entity.mesh.StorageMesh
import light.DirectionalLight
import logging.Logger
import java.io.*

class Scene : LinkedHashMap<String, Entity>(), Serializable {
    val meshes = LinkedHashMap<String, StorageMesh>()
    val textures = LinkedHashMap<String, Texture>()
    val materials = LinkedHashMap<String, Material>()

    var selectedDirectionalLight = DirectionalLight()
    var selectedEntity: Entity? = null

    var camera = Camera()

    init {
        Logger.log("Created empty scene")
    }

    fun getSlectedName(): String? {
        for (entity in this) {
            if (entity.value === selectedEntity) {
                return entity.key
            }
        }
        return null
    }

    fun saveToFile(filepath: String) {
        Logger.log("Scene saving stared ($filepath)")
        return try {
            ObjectOutputStream(FileOutputStream(filepath)).use {
                it.writeObject(this)
            }
            Logger.log("Saved the scene ($filepath)")
        }
        catch (e: Exception) {
            e.printStackTrace()
            Logger.log("Failed to save the scene ($filepath)")
        }
    }

    companion object {
        fun loadFromFile(filepath: String): Scene {
            Logger.log("Scene loading started ($filepath)")
            return try {
                ObjectInputStream(FileInputStream(filepath)).use {
                    val result = it.readObject() as Scene
                    Logger.log("Loaded the scene ($filepath)")
                    return result
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                Logger.log("Failed to load the scene ($filepath)")
                Scene()
            }
        }
    }
}
