package math.vector

import java.io.Serializable
import kotlin.math.sqrt

class Vector2 : Serializable {
    var x: Double = 0.0
    var y: Double = 0.0

    constructor()

    constructor(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    constructor(a: Double) : this(a, a)

    constructor(data: DoubleArray) : this(data[0], data[1])

    constructor(v: Vector2) : this(v.x, v.y)

    // Getters
    val length: Double
        get() = sqrt(this * this)

    val array: DoubleArray
        get() = doubleArrayOf(x, y)

    // Math
    operator fun plus(v: Vector2): Vector2 {
        return Vector2(x + v.x, y + v.y)
    }

    operator fun minus(v: Vector2): Vector2 {
        return Vector2(x - v.x, y - v.y)
    }

    operator fun times(a: Double): Vector2 {
        return Vector2(x * a, y * a)
    }

    operator fun times(v: Vector2): Double {
        return (x * v.x + y * v.y)
    }

    operator fun div(a: Double): Vector2 {
        return this * (1.0 / a)
    }

    operator fun unaryMinus(): Vector2 {
        return this * -1.0
    }

    override fun equals(other: Any?): Boolean {
        if (other is Vector2) {
            return (x == other.x && y == other.y)
        }
        return false
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    companion object {
        val LEFT: Vector2
            get() = Vector2(-1.0, 0.0)
        val RIGHT: Vector2
            get() = Vector2(1.0, 0.0)

        val DOWN: Vector2
            get() = Vector2(0.0, -1.0)
        val UP: Vector2
            get() = Vector2(0.0, 1.0)
    }
}