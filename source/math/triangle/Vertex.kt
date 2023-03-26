package math.triangle

import math.normalize
import math.vector.Vector2
import math.vector.Vector3
import java.io.Serializable

class Vertex : Serializable {
    var world = Vector3()
    var texture = Vector2()
    var normal = Vector3()
        set(normal) { field = normalize(normal) }

    constructor() {
        world = Vector3()
        texture = Vector2()
        normal = Vector3()
    }

    constructor(world: Vector3) {
        this.world = world
        texture = Vector2()
        normal = Vector3()
    }

    constructor(world: Vector3, texture: Vector2) {
        this.world = world
        this.texture = texture
        normal = Vector3()
    }

    constructor(world: Vector3, texture: Vector2, normal: Vector3) {
        this.world = world
        this.texture = texture
        this.normal = normal
    }

    constructor(vertex: Vertex) {
        world = Vector3(vertex.world)
        texture = Vector2(vertex.texture)
        normal = Vector3(vertex.normal)
    }

    override fun toString(): String {
        return "($world, $texture, $normal)"
    }
}
