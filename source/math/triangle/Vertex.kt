package math.triangle

import math.normalize
import math.vector.Float2
import math.vector.Float3
import java.io.Serializable

class Vertex : Serializable {
    var world: Float3 = Float3()
    var texture: Float2 = Float2()
    var normal: Float3 = Float3()
        set(normal) { field = normalize(normal) }

    constructor() {
        world = Float3()
        texture = Float2()
        normal = Float3()
    }

    constructor(world: Float3) {
        this.world = world
        texture = Float2()
        normal = Float3()
    }

    constructor(world: Float3, texture: Float2) {
        this.world = world
        this.texture = texture
        normal = Float3()
    }

    constructor(world: Float3, texture: Float2, normal: Float3) {
        this.world = world
        this.texture = texture
        this.normal = normal
    }

    constructor(vertex: Vertex) {
        world = Float3(vertex.world)
        texture = Float2(vertex.texture)
        normal = Float3(vertex.normal)
    }

    override fun toString(): String {
        return "($world, $texture, $normal)"
    }
}