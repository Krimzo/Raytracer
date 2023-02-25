package entity.mesh

import math.triangle.Triangle
import math.triangle.Vertex
import math.vector.Float2
import math.vector.Float3
import java.io.BufferedReader
import java.io.FileReader
import java.io.Serializable
import java.lang.Float.max

class Mesh : ArrayList<Triangle>, Serializable {
    val maxRadius: Float

    constructor() {
        maxRadius = 0f
    }

    constructor(filepath: String) {
        // Parse file
        try {
            val file = FileReader(filepath)
            val reader = BufferedReader(file)

            val vertices = ArrayList<Vertex>()
            val xyzBuffer = ArrayList<Float3>()
            val uvBuffer = ArrayList<Float2>()
            val normBuffer = ArrayList<Float3>()

            var fileLine = ""
            while (reader.readLine()?.let { fileLine = it } != null) {
                val lineParts = fileLine.split(" ")
                when (lineParts[0]) {
                    "v" -> {
                        xyzBuffer.add(Float3(lineParts[1].toFloat(), lineParts[2].toFloat(), lineParts[3].toFloat()))
                    }
                    "vt" -> {
                        uvBuffer.add(Float2(lineParts[1].toFloat(), lineParts[2].toFloat()))
                    }
                    "vn" -> {
                        normBuffer.add(Float3(lineParts[1].toFloat(), lineParts[2].toFloat(), lineParts[3].toFloat()))
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
        var maxRadius = 0f
        for (triangle in this) {
            maxRadius = max(maxRadius, triangle.a.world.length)
            maxRadius = max(maxRadius, triangle.b.world.length)
            maxRadius = max(maxRadius, triangle.c.world.length)
        }
        this.maxRadius = maxRadius
    }
}
