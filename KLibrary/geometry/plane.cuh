#pragma once

#include "math/float3.cuh"

#include "cuda/kcuda.cuh"


namespace kl {
	class plane {
	public:
		kl::float3 normal;
		kl::float3 point;

		ALL plane() {}
		ALL plane(const kl::float3& normal, const kl::float3& point) : normal(normal.normalize()), point(point) {}
	};
}
