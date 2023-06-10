package scene

import math.ray.Sphere
import java.io.*

class Scene : ArrayList<Sphere>(), Serializable {
    var camera = Camera()

    fun saveToFile(filepath: String) {
        return try {
            ObjectOutputStream(FileOutputStream(filepath)).use {
                it.writeObject(this)
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun loadFromFile(filepath: String): Scene {
            return try {
                ObjectInputStream(FileInputStream(filepath)).use {
                    return it.readObject() as Scene
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                Scene()
            }
        }
    }
}
