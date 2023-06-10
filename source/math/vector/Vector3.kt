package math.vector

import java.awt.Color
import java.io.Serializable
import kotlin.math.sqrt

class Vector3 : Serializable {
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0

    constructor()

    constructor(x: Double, y: Double, z: Double) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(a: Double) : this(a, a, a)

    constructor(data: DoubleArray) : this(data[0], data[1], data[2])

    constructor(v: Vector2, z: Double) : this(v.x, v.y, z)

    constructor(x: Double, v: Vector2) : this(x, v.x, v.y)

    constructor(v: Vector3) : this(v.x, v.y, v.z)

    constructor(color: Color) {
        val conv = 1.0 / 255.0
        x = color.red * conv
        y = color.green * conv
        z = color.blue * conv
    }

    // Getters
    val length: Double
        get() = sqrt(this * this)

    val xy: Vector2
        get() = Vector2(x, y)

    val array: DoubleArray
        get() = doubleArrayOf(x, y, z)

    val color: Color
        get() = Color(
            (x * 255).toInt().coerceAtLeast(0).coerceAtMost(255),
            (y * 255).toInt().coerceAtLeast(0).coerceAtMost(255),
            (z * 255).toInt().coerceAtLeast(0).coerceAtMost(255)
        )

    // Math
    operator fun plus(v: Vector3): Vector3 {
        return Vector3(x + v.x, y + v.y, z + v.z)
    }

    operator fun minus(v: Vector3): Vector3 {
        return Vector3(x - v.x, y - v.y, z - v.z)
    }

    operator fun times(a: Double): Vector3 {
        return Vector3(x * a, y * a, z * a)
    }

    operator fun times(v: Vector3): Double {
        return (x * v.x + y * v.y + z * v.z)
    }

    infix fun x(v: Vector3): Vector3 {
        return Vector3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x)
    }

    operator fun div(a: Double): Vector3 {
        return this * (1.0 / a)
    }

    operator fun unaryMinus(): Vector3 {
        return this * -1.0
    }

    infix fun multiply(other: Vector3): Vector3 {
        return Vector3(
            x * other.x,
            y * other.y,
            z * other.z,
        )
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Vector3) (x == other.x && y == other.y && z == other.z) else false
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    companion object {
        val LEFT: Vector3
            get() = Vector3(-1.0, 0.0, 0.0)
        val RIGHT: Vector3
            get() = Vector3(1.0, 0.0, 0.0)

        val DOWN: Vector3
            get() = Vector3(0.0, -1.0, 0.0)
        val UP: Vector3
            get() = Vector3(0.0, 1.0, 0.0)

        val BACK: Vector3
            get() = Vector3(0.0, 0.0, -1.0)
        val FORWARD: Vector3
            get() = Vector3(0.0, 0.0, 1.0)
    }
}