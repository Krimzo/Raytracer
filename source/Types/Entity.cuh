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

		struct mesh {
			size_t size = 0;
			kl::triangle* buffer = nullptr;
			kl::float3 far = 0.0f;
			struct computed {
				size_t size = 0;
				kl::triangle* buffer = nullptr;
				float far = 0.0f;

				computed() {}
				computed(size_t size) : size(size) {
					kl::cuda::alloc(buffer, size);
				}
				computed(const computed& obj) : size(obj.size), far(obj.far) {
					kl::cuda::alloc(buffer, size);
					kl::cuda::copy(buffer, obj.buffer, size, kl::cuda::transfer::DD);
				}
				void operator=(const computed& obj) {
					size = obj.size;
					far = obj.far;
					kl::cuda::realloc(buffer, size);
					kl::cuda::copy(buffer, obj.buffer, size, kl::cuda::transfer::DD);
				}
				~computed() {
					kl::cuda::free(buffer);
					size = 0;
				}
				void resize(size_t newSize) {
					size = newSize;
					kl::cuda::realloc(buffer, newSize);
				}
			} computed;

			mesh() {}
			mesh(const Raytracer::Mesh& mes) : size(mes.size), buffer(mes.buffer), far(mes.far), computed(mes.size) {}
			void operator=(const Raytracer::Mesh& mes) {
				size = mes.size;
				buffer = mes.buffer;
				far = mes.far;
				computed.resize(mes.size);
			}
		} mesh;

		struct texture {
			kl::color* buffer = nullptr;
			kl::int2 size = {};
			float roughness = 1.0f;

			texture() {}
			texture(const Raytracer::Texture& tex) : buffer(tex.buffer), size(tex.size) {}
			void operator=(const Raytracer::Texture& tex) {
				buffer = tex.buffer;
				size = tex.size;
			}
		} texture;

		Entity() {}
		Entity(const Raytracer::Mesh& mesh, const Raytracer::Texture& texture) : mesh(mesh), texture(texture) {}

		ALL void updatePhys(float deltaT) {
			position += velocity * deltaT;
			rotation += angular * deltaT;
		}

		ALL kl::mat4 matrix() const {
			return kl::mat4::translate(position) * kl::mat4::rotate(rotation) * kl::mat4::scale(scale);
		}
	};
}
