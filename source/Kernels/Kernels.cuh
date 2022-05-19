#pragma once

#include "Raytracer.cuh"


namespace Kernels {
	EXEC void Precompute(size_t entityCount, Raytracer::Entity* entities);
	EXEC void Raytrace(size_t pixelCount, kl::color* pixelBuffer, kl::int2 screenSize, kl::camera camera, Raytracer::Entity* entities, size_t entityCount);
}
