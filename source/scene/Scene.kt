package scene

import entity.Entity
import scene.light.DirectionalLight
import java.io.Serializable

class Scene : LinkedHashMap<String, Entity>(), Serializable {
    var camera = Camera()
    var directionalLight = DirectionalLight()
}
