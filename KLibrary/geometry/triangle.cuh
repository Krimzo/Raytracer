#pragma once

#include "math/float2.cuh"
#include "math/float3.cuh"
#include "math/float4.cuh"
#include "geometry/vertex.cuh"

#include "cuda/kcuda.cuh"


namespace kl {
	class triangle {
	private:
		ALL kl::float3 getWeights(const kl::float4& interConsts, const kl::float2& pos) const {
			const float dx = pos.x - c.world.x;
			const float dy = pos.y - c.world.y;
			const float interWeight1 = dx * interConsts.x + dy * interConsts.y;
			const float interWeight2 = dx * interConsts.z + dy * interConsts.w;
			return kl::float3(interWeight1, interWeight2, 1.0f - interWeight1 - interWeight2);
		}

	public:
		kl::vertex a, b, c;

		ALL triangle() {}
		ALL triangle(const kl::vertex& a, const kl::vertex& b, const kl::vertex& c) : a(a), b(b), c(c) {}

		ALL kl::float3 normal() {
			const kl::float3 v1 = b.world - a.world;
			const kl::float3 v2 = c.world - a.world;
			return v2.cross(v1).normalize();
		}

		ALL kl::float4 getConsts() {
			const float tempConst = 1.0f / ((b.world.y - c.world.y) * (a.world.x - c.world.x) + (c.world.x - b.world.x) * (a.world.y - c.world.y));
			return kl::float4(
				(b.world.y - c.world.y) * tempConst,
				(c.world.x - b.world.x) * tempConst,
				(c.world.y - a.world.y) * tempConst,
				(a.world.x - c.world.x) * tempConst
			);
		}

		ALL bool inTriangle(const kl::float4& interConsts, const kl::float2& pos) const {
			const kl::float3 weights = getWeights(interConsts, pos);
			return !(weights.x < 0.0f || weights.y < 0.0f || weights.z < 0.0f);
		}

		ALL float interpolate(const kl::float4& interConsts, const kl::float3& values, const kl::float2& pos) const {
			const kl::float3 weights = getWeights(interConsts, pos);
			return values.x * weights.x + values.y * weights.y + values.z * weights.z;
		}
		ALL kl::vertex interpolate(const kl::float4& interConsts, const kl::float2& pos) const {
			const kl::float3 weights = getWeights(interConsts, pos);
			return kl::vertex(
				kl::float3(
					a.world.x * weights.x + b.world.x * weights.y + c.world.x * weights.z,
					a.world.y * weights.x + b.world.y * weights.y + c.world.y * weights.z,
					a.world.z * weights.x + b.world.z * weights.y + c.world.z * weights.z
				),
				kl::float2(
					a.texture.x * weights.x + b.texture.x * weights.y + c.texture.x * weights.z,
					a.texture.y * weights.x + b.texture.y * weights.y + c.texture.y * weights.z
				),
				kl::float3(
					a.normal.x * weights.x + b.normal.x * weights.y + c.normal.x * weights.z,
					a.normal.y * weights.x + b.normal.y * weights.y + c.normal.y * weights.z,
					a.normal.z * weights.x + b.normal.z * weights.y + c.normal.z * weights.z
				)
			);
		}
		ALL kl::vertex interpolate(const kl::float3& point) const {
			const kl::float3 v0 = a.world - c.world;
			const kl::float3 v1 = b.world - c.world;
			const kl::float3 v2 = point - c.world;
			const float d00 = v0.dot(v0);
			const float d01 = v0.dot(v1);
			const float d11 = v1.dot(v1);
			const float d20 = v2.dot(v0);
			const float d21 = v2.dot(v1);
			const float invDenom = 1.0f / (d00 * d11 - d01 * d01);
			const float w1 = (d11 * d20 - d01 * d21) * invDenom;
			const float w2 = (d00 * d21 - d01 * d20) * invDenom;
			const float w3 = 1.0f - w1 - w2;
			return {
				point,
				a.texture * w1 + b.texture * w2 + c.texture * w3,
				a.normal * w1 + b.normal * w2 + c.normal * w3
			};
		}
	};
}