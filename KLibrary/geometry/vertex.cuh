#pragma once 

#include "math/float3.cuh"

#include "cuda/kcuda.cuh"


namespace kl {
	class vertex {
	public:
		kl::float3 world;
		kl::float2 texture;
		kl::float3 normal;

		ALL vertex() {}
		ALL vertex(const kl::float3& world) : world(world) {}
		ALL vertex(const kl::float3& world, const kl::float2& texture) : world(world), texture(texture) {}
		ALL vertex(const kl::float3& world, const kl::float3& normal) : world(world), normal(normal) {}
		ALL vertex(const kl::float3& world, const kl::float2& texture, const kl::float3& normal) : world(world), texture(texture), normal(normal) {}

		ALL bool equals(const kl::vertex& obj) const {
			return world == obj.world && texture == obj.texture && normal == obj.normal;
		}
		ALL bool operator==(const kl::vertex& obj) const {
			return equals(obj);
		}
		ALL bool operator!=(const kl::vertex& obj) const {
			return !equals(obj);
		}
	};
}