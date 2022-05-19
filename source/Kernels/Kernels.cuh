#pragma once

#include "Raytracer.cuh"


namespace Kernels {
	EXEC void Physics(size_t entityCount, Raytracer::Entity* entities, float deltaT);
	EXEC void Precompute(size_t triangleCount, Raytracer::Entity* entities, size_t entityCount);
	EXEC void Raytrace(size_t pixelCount, kl::color* pixelBuffer, kl::int2 screenSize, kl::float3 camPos, kl::mat4 invCam, Raytracer::Entity* entities, size_t entityCount);
}
