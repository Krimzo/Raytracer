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

		Raytracer::Mesh* mesh = nullptr;
		Raytracer::Mesh computed;

		Raytracer::Texture* texture = nullptr;
		float roughness = 1.0f;

		Entity(Raytracer::Mesh* mesh, Raytracer::Texture* texture) : mesh(mesh), computed(*mesh), texture(texture) {}

		void setMesh(Raytracer::Mesh* newMesh) {
			mesh = newMesh;
			computed.resize(newMesh->size);
		}

		ALL void updatePhys(float deltaT) {
			position += velocity * deltaT;
			rotation += angular * deltaT;
		}

		ALL kl::mat4 matrix() const {
			return kl::mat4::translation(position) * kl::mat4::rotation(rotation) * kl::mat4::scaling(scale);
		}
	};
}
