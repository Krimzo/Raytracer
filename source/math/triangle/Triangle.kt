package math.triangle

import math.vector.Vector2
import math.vector.Vector3

import java.io.Serializable

class Triangle : Serializable {
    var a = Vertex()
    var b = Vertex()
    var c = Vertex()

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

    fun interpolate(position: Vector3): Vertex {
        val weights = getInterpolationWeights(position)
        return Vertex(
        Vector3(
            weights * Vector3(a.world.x, b.world.x, c.world.x),
            weights * Vector3(a.world.y, b.world.y, c.world.y),
            weights * Vector3(a.world.z, b.world.z, c.world.z),
        ),
        Vector2(
            weights * Vector3(a.texture.x, b.texture.x, c.texture.x),
            weights * Vector3(a.texture.y, b.texture.y, c.texture.y),
        ),
        Vector3(
            weights * Vector3(a.normal.x, b.normal.x, c.normal.x),
            weights * Vector3(a.normal.y, b.normal.y, c.normal.y),
            weights * Vector3(a.normal.z, b.normal.z, c.normal.z),
        ))
    }

    private fun getInterpolationWeights(position: Vector3): Vector3 {
        val v0 = a.world - c.world
        val v1 = b.world - c.world
        val v2 = position - c.world

        val d00 = v0 * v0
        val d01 = v0 * v1
        val d11 = v1 * v1
        val d20 = v2 * v0
        val d21 = v2 * v1

        val invDenom = 1.0 / (d00 * d11 - d01 * d01)
        val w1 = (d11 * d20 - d01 * d21) * invDenom
        val w2 = (d00 * d21 - d01 * d20) * invDenom

        return Vector3(w1, w2, 1.0 - w1 - w2)
    }
}