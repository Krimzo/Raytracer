#pragma once

#include "KrimzLib.cuh"


namespace Raytracer {
	struct Texture {
		kl::color* buffer = nullptr;
		kl::int2 size = {};
		size_t len = 0;

		Texture(const kl::int2& size) : size(size), len(size.x* size.y) {
			if (cudaMallocManaged(&buffer, len * sizeof(kl::color))) {
				exit(69);
			}
		}
		Texture(const kl::color* pixels, const kl::int2& size) : Texture(size) {
			cudaMemcpy(buffer, pixels, len * sizeof(kl::color), cudaMemcpyHostToDevice);
		}
		Texture(const kl::image& img) : Texture(img.pointer(), img.size()) {}
		Texture(const Texture& obj) : Texture(obj.buffer, obj.size) {}
		void operator=(const Texture& obj) {
			size = obj.size;
			len = obj.len;
			cudaFree(buffer);
			if (cudaMallocManaged(&buffer, obj.len * sizeof(kl::color))) {
				exit(69);
			}
			cudaMemcpy(buffer, obj.buffer, obj.len * sizeof(kl::color), cudaMemcpyDeviceToDevice);
		}
		~Texture() {
			cudaFree(buffer);
			buffer = nullptr;
		}
	};
}
