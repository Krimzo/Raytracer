#pragma once

#include "Raytracer.cuh"


namespace Kernels {
	RUN void Physics(uint64 entityCount, Raytracer::Entity* entities, float deltaT);
	RUN void Precompute(uint64 triangleCount, Raytracer::Entity* entities, uint64 entityCount);
	RUN void Raytrace(uint64 pixelCount, kl::color* pixelBuffer, kl::int2 screenSize, kl::float3 camPos, kl::mat4 invCam, Raytracer::Entity* entities, uint64 entityCount, kl::float3 sunDir);

	inline kl::cuda::kernel physics = Physics;
	inline kl::cuda::kernel precompute = Precompute;
	inline kl::cuda::kernel raytrace = Raytrace;
}
