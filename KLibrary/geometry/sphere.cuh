#pragma once

#include "math/float3.cuh"

#include "cuda/kcuda.cuh"


namespace kl {
	class sphere {
	public:
		// Geometry
		kl::float3 center;
		float radius = 0.0f;

		// Light
		kl::float3 color;
		float reflectivity = 0.0f;
		float emission = 0.0f;

		ALL sphere() {}
		ALL sphere(const kl::float3& center, float radius) : center(center), radius(radius) {}
		ALL sphere(const kl::float3& center, float radius, const kl::float3& color, float reflectivity, float emission) : center(center), radius(radius), color(color), reflectivity(reflectivity), emission(emission) {}

		ALL kl::float3 calcEmiss() const {
			return color * emission;
		}
	};
}
