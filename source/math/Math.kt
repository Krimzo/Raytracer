package math

import math.vector.Float2
import math.vector.Float3
import math.vector.Float4
import math.vector.Int2
import kotlin.math.abs

// Int2
fun abs(vec: Int2): Int2 {
    return Int2(abs(vec.x), abs(vec.y))
}

// Float
fun toRadians(value: Float): Float {
    return Math.toRadians(value.toDouble()).toFloat()
}

fun toDegrees(value: Float): Float {
    return Math.toDegrees(value.toDouble()).toFloat()
}

// Float2
fun abs(vec: Float2): Float2 {
    return Float2(abs(vec.x), abs(vec.y))
}

fun normalize(vec: Float2): Float2 {
    return vec / vec.length
}

fun toRadians(vec: Float2): Float2 {
    return Float2(toRadians(vec.x), toRadians(vec.y))
}

fun toDegrees(vec: Float2): Float2 {
    return Float2(toDegrees(vec.x), toDegrees(vec.y))
}

// Float3
fun abs(vec: Float3): Float3 {
    return Float3(abs(vec.x), abs(vec.y), abs(vec.z))
}

fun normalize(vec: Float3): Float3 {
    return vec / vec.length
}

fun toRadians(vec: Float3): Float3 {
    return Float3(toRadians(vec.x), toRadians(vec.y), toRadians(vec.z))
}

fun toDegrees(vec: Float3): Float3 {
    return Float3(toDegrees(vec.x), toDegrees(vec.y), toDegrees(vec.z))
}

// Float4
fun abs(vec: Float4): Float2 {
    return Float2(abs(vec.x), abs(vec.y))
}

fun normalize(vec: Float4): Float4 {
    return vec / vec.length
}

fun toRadians(vec: Float4): Float4 {
    return Float4(toRadians(vec.x), toRadians(vec.y), toDegrees(vec.z), toDegrees(vec.w))
}

fun toDegrees(vec: Float4): Float4 {
    return Float4(toDegrees(vec.x), toDegrees(vec.y), toDegrees(vec.z), toDegrees(vec.w))
}
