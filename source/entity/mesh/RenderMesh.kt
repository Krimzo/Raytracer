package entity.mesh

import math.matrix.Matrix4x4
import math.triangle.Triangle
import math.vector.Vector2
import math.vector.Vector4
import java.io.Serializable
import kotlin.math.max

class RenderMesh : ArrayList<Triangle>, Serializable {
    val maxRadius: Double

    constructor() {
        maxRadius = 0.0
    }

    constructor(storageMesh: StorageMesh, scaling: Matrix4x4, rotationTranslation: Matrix4x4) {
        val fullTransform = rotationTranslation * scaling
        var maxRadius = 0.0

        for (triangle in storageMesh) {
            val transformedTriangle = Triangle()

            // World-Scaling
            transformedTriangle.a.world = (scaling * Vector4(triangle.a.world, 1.0)).xyz
            transformedTriangle.b.world = (scaling * Vector4(triangle.b.world, 1.0)).xyz
            transformedTriangle.c.world = (scaling * Vector4(triangle.c.world, 1.0)).xyz

            // Max radius
            maxRadius = max(maxRadius, transformedTriangle.a.world.length)
            maxRadius = max(maxRadius, transformedTriangle.b.world.length)
            maxRadius = max(maxRadius, transformedTriangle.c.world.length)

            // World-Rotation-Translation
            transformedTriangle.a.world = (rotationTranslation * Vector4(transformedTriangle.a.world, 1.0)).xyz
            transformedTriangle.b.world = (rotationTranslation * Vector4(transformedTriangle.b.world, 1.0)).xyz
            transformedTriangle.c.world = (rotationTranslation * Vector4(transformedTriangle.c.world, 1.0)).xyz

            // Texture
            transformedTriangle.a.texture = Vector2(triangle.a.texture)
            transformedTriangle.b.texture = Vector2(triangle.b.texture)
            transformedTriangle.c.texture = Vector2(triangle.c.texture)

            // Normal
            transformedTriangle.a.normal = (fullTransform * Vector4(triangle.a.normal, 0.0)).xyz
            transformedTriangle.b.normal = (fullTransform * Vector4(triangle.b.normal, 0.0)).xyz
            transformedTriangle.c.normal = (fullTransform * Vector4(triangle.c.normal, 0.0)).xyz

            this.add(transformedTriangle)
        }
        this.maxRadius = maxRadius
    }
}
