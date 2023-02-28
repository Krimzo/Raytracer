package scene

import entity.Entity
import entity.material.Material
import entity.material.Texture
import entity.mesh.StorageMesh
import logging.Logger
import scene.light.DirectionalLight
import java.io.*
import java.lang.Exception

class Scene : LinkedHashMap<String, Entity>(), Serializable {
    val meshes = LinkedHashMap<String, StorageMesh>()
    val textures = LinkedHashMap<String, Texture>()
    val materials = LinkedHashMap<String, Material>()

    var directionalLight = DirectionalLight()

    var camera = Camera()

    init {
        Logger.log("Created empty scene")
    }

    fun saveToFile(filepath: String) {
        Logger.log("Started saving a scene ($filepath)")
        return try {
            ObjectOutputStream(FileOutputStream(filepath)).use {
                it.writeObject(this)
            }
            Logger.log("Saved a scene ($filepath)")
        }
        catch (e: Exception) {
            e.printStackTrace()
            Logger.log("Failed to save a scene ($filepath)")
        }
    }

    companion object {
        fun loadFromFile(filepath: String): Scene {
            Logger.log("Started loading a scene ($filepath)")
            return try {
                ObjectInputStream(FileInputStream(filepath)).use {
                    val result = it.readObject() as Scene
                    Logger.log("Loaded a scene ($filepath)")
                    return result
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                Logger.log("Failed to load a scene ($filepath)")
                Scene()
            }
        }
    }
}
