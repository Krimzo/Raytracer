package math.vector

import java.awt.Color
import java.io.Serializable
import kotlin.math.sqrt

class Vector4 : Serializable {
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0
    var w: Double = 0.0

    constructor()

    constructor(x: Double, y: Double, z: Double, w: Double) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    constructor(a: Double) : this(a, a, a, a)

    constructor(data: DoubleArray) : this(data[0], data[1], data[2], data[3])

    constructor(v: Vector2, z: Double, w: Double) : this(v.x, v.y, z, w)

    constructor(x: Double, v: Vector2, w: Double) : this(x, v.x, v.y, w)

    constructor(x: Double, y: Double, v: Vector2) : this(x, y, v.x, v.y)

    constructor(v1: Vector2, v2: Vector2) : this(v1.x, v1.y, v2.x, v2.y)

    constructor(v: Vector3, w: Double) : this(v.x, v.y, v.z, w)

    constructor(x: Double, v: Vector3) : this(x, v.x, v.y, v.z)

    constructor(v: Vector4) : this(v.x, v.y, v.z, v.w)

    constructor(color: Color) : this(Vector3(color), color.alpha / 255.0)

    // Getters
    val length: Double
        get() = sqrt(this * this)

    val xy: Vector2
        get() = Vector2(x, y)

    val xyz: Vector3
        get() = Vector3(x, y, z)

    val array: DoubleArray
        get() = doubleArrayOf(x, y, z, w)

    val color: Color
        get() = Color(
            (x * 255).toInt().coerceAtLeast(0).coerceAtMost(255),
            (y * 255).toInt().coerceAtLeast(0).coerceAtMost(255),
            (z * 255).toInt().coerceAtLeast(0).coerceAtMost(255),
            (w * 255).toInt().coerceAtLeast(0).coerceAtMost(255)
        )

    // Math
    operator fun plus(v: Vector4): Vector4 {
        return Vector4(x + v.x, y + v.y, z + v.z, w + v.w)
    }

    operator fun minus(v: Vector4): Vector4 {
        return Vector4(x - v.x, y - v.y, z - v.z, w - v.w)
    }

    operator fun times(a: Double): Vector4 {
        return Vector4(x * a, y * a, z * a, w * a)
    }

    operator fun times(v: Vector4): Double {
        return (x * v.x + y * v.y + z * v.z + w * v.w)
    }

    operator fun div(a: Double): Vector4 {
        return this * (1.0 / a)
    }

    operator fun unaryMinus(): Vector4 {
        return this * -1.0
    }

    override fun equals(other: Any?): Boolean {
        if (other is Vector4) {
            return (x == other.x && y == other.y && z == other.z && w == other.w)
        }
        return false
    }

    override fun toString(): String {
        return "($x, $y, $z, $w)"
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + w.hashCode()
        return result
    }
}
