#pragma once

#include "KrimzLib.h"


namespace Raytracer {
	struct Mesh {
		uint64 size = 0;
		kl::triangle* buffer = nullptr;
		kl::float3 far = 0.0f;

		Mesh(uint64 size) : size(size) {
			kl::cuda::alloc(buffer, size);
		}
		Mesh(const std::vector<kl::vertex>& vertices) : Mesh(vertices.size() / 3) {
			kl::cuda::copy(buffer, &vertices[0], size, kl::cuda::transfer::HD);
			for (auto& vert : vertices) {
				if (vert.world.length() > far.length()) {
					far = vert.world;
				}
			}
		}
		Mesh(const std::string& filePath) : Mesh(kl::file::parseMesh(filePath)) {}
		Mesh(const Mesh& obj) : size(obj.size), far(obj.far) {
			kl::cuda::alloc(buffer, obj.size);
			kl::cuda::copy(buffer, obj.buffer, obj.size, kl::cuda::transfer::DD);
		}
		void operator=(const Mesh& obj) {
			size = obj.size;
			far = obj.far;
			kl::cuda::realloc(buffer, obj.size);
			kl::cuda::copy(buffer, obj.buffer, obj.size, kl::cuda::transfer::DD);
		}
		~Mesh() {
			kl::cuda::free(buffer);
		}

		bool resize(uint64 newSize) {
			if (newSize != size) {
				size = newSize;
				kl::cuda::realloc(buffer, newSize);
				return true;
			}
			return false;
		}
	};
}
