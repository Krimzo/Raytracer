package math.triangle

import math.vector.Float2
import math.vector.Float3

import java.io.Serializable

class Triangle : Serializable {
    var a: Vertex = Vertex()
    var b: Vertex = Vertex()
    var c: Vertex = Vertex()

    constructor()

    constructor(a: Vertex, b: Vertex, c: Vertex) {
        this.a = a
        this.b = b
        this.c = c
    }

    constructor(triangle: Triangle) {
        a = Vertex(triangle.a)
        b = Vertex(triangle.b)
        c = Vertex(triangle.c)
    }

    fun interpolate(position: Float3): Vertex {
        val weights = getInterpolationWeights(position)
        return Vertex(
        Float3(
            weights * Float3(a.world.x, b.world.x, c.world.x),
            weights * Float3(a.world.y, b.world.y, c.world.y),
            weights * Float3(a.world.z, b.world.z, c.world.z),
        ),
        Float2(
            weights * Float3(a.texture.x, b.texture.x, c.texture.x),
            weights * Float3(a.texture.y, b.texture.y, c.texture.y),
        ),
        Float3(
            weights * Float3(a.normal.x, b.normal.x, c.normal.x),
            weights * Float3(a.normal.y, b.normal.y, c.normal.y),
            weights * Float3(a.normal.z, b.normal.z, c.normal.z),
        ))
    }

    private fun getInterpolationWeights(position: Float3): Float3 {
        val v0 = a.world - c.world
        val v1 = b.world - c.world
        val v2 = position - c.world

        val d00 = v0 * v0
        val d01 = v0 * v1
        val d11 = v1 * v1
        val d20 = v2 * v0
        val d21 = v2 * v1

        val invDenom = 1f / (d00 * d11 - d01 * d01)
        val w1 = (d11 * d20 - d01 * d21) * invDenom
        val w2 = (d00 * d21 - d01 * d20) * invDenom

        return Float3(w1, w2, 1f - w1 - w2)
    }
}