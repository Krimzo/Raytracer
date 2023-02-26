package entity.mesh

import math.triangle.Triangle
import math.triangle.Vertex
import math.vector.Vector2
import math.vector.Vector3
import java.io.BufferedReader
import java.io.FileReader
import java.io.Serializable
import kotlin.math.max

class Mesh : ArrayList<Triangle>, Serializable {
    val maxRadius: Double

    constructor() {
        maxRadius = 0.0
    }

    constructor(filepath: String, flipZ: Boolean = true) {
        // Parse file
        try {
            val file = FileReader(filepath)
            val reader = BufferedReader(file)

            val vertices = ArrayList<Vertex>()
            val xyzBuffer = ArrayList<Vector3>()
            val uvBuffer = ArrayList<Vector2>()
            val normBuffer = ArrayList<Vector3>()

            val zFlip = if (flipZ) -1.0 else 1.0

            var fileLine = ""
            while (reader.readLine()?.let { fileLine = it } != null) {
                val lineParts = fileLine.split(" ")
                when (lineParts[0]) {
                    "v" -> {
                        xyzBuffer.add(Vector3(lineParts[1].toDouble(), lineParts[2].toDouble(), lineParts[3].toDouble() * zFlip))
                    }
                    "vt" -> {
                        uvBuffer.add(Vector2(lineParts[1].toDouble(), lineParts[2].toDouble()))
                    }
                    "vn" -> {
                        normBuffer.add(Vector3(lineParts[1].toDouble(), lineParts[2].toDouble(), lineParts[3].toDouble() * zFlip))
                    }
                    "f" -> {
                        for (i in 1..3) {
                            val linePartParts = lineParts[i].split("/")
                            vertices.add(Vertex(
                                xyzBuffer[linePartParts[0].toInt() - 1],
                                uvBuffer[linePartParts[1].toInt() - 1],
                                normBuffer[linePartParts[2].toInt() - 1]
                            ))
                        }
                    }
                }
            }
            reader.close()
            file.close()

            for (i in 0 until (vertices.size / 3)) {
                val triangle = Triangle()
                triangle.a = vertices[i * 3 + 0]
                triangle.b = vertices[i * 3 + 1]
                triangle.c = vertices[i * 3 + 2]
                add(triangle)
            }
        }
        catch (ignored: Exception) {
            println("Parse mesh error! [$filepath]")
        }

        // Compute max radius
        var maxRadius = 0.0
        for (triangle in this) {
            maxRadius = max(maxRadius, triangle.a.world.length)
            maxRadius = max(maxRadius, triangle.b.world.length)
            maxRadius = max(maxRadius, triangle.c.world.length)
        }
        this.maxRadius = maxRadius
    }
}
