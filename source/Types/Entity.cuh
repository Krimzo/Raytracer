#pragma once

#include "Types/Mesh.cuh"
#include "Types/Texture.cuh"


namespace Raytracer {
	class Entity {
	public:
		kl::float3 scale = kl::float3(1.0f);
		kl::float3 rotation;
		kl::float3 position;

		kl::float3 velocity;
		kl::float3 angular;

		size_t meshSize = 0;
		kl::triangle* meshOrig = nullptr;
		kl::float3 meshFar = 0.0f;

		kl::color* textureBuffer = nullptr;
		kl::int2 textureSize;
		float roughness = 1.0f;

		Entity() {}
		Entity(const Raytracer::Mesh& mesh, const Raytracer::Texture& texture) {
			setMesh(mesh);
			setTexture(texture);
		}

		void setMesh(const Mesh& mesh) {
			meshSize = mesh.size;
			meshOrig = mesh.original;
			meshFar = mesh.far;
		}
		void setTexture(const Texture& texture) {
			textureBuffer = texture.buffer;
			textureSize = texture.size;
		}

		void updatePhys(float deltaT) {
			position += velocity * deltaT;
			rotation += angular * deltaT;
		}

		ALL kl::mat4 matrix() const {
			return kl::mat4::translate(position) * kl::mat4::rotate(rotation) * kl::mat4::scale(scale);
		}
	};
}
