#pragma once

#include "KrimzLib.cuh"


namespace Raytracer {
	struct Texture {
		kl::color* buffer = nullptr;
		kl::int2 size = {};
		size_t len = 0;

		Texture(const kl::int2& size) : size(size), len(size.x* size.y) {
			kl::cuda::alloc(buffer, len);
		}
		Texture(const kl::color* pixels, const kl::int2& size) : Texture(size) {
			kl::cuda::copy(buffer, pixels, len, kl::cuda::transfer::HD);
		}
		Texture(const kl::image& img) : Texture(img.pointer(), img.size()) {}
		Texture(const Texture& obj) : Texture(obj.buffer, obj.size) {}
		void operator=(const Texture& obj) {
			size = obj.size;
			len = obj.len;
			kl::cuda::realloc(buffer, obj.len);
			kl::cuda::copy(buffer, obj.buffer, obj.len, kl::cuda::transfer::DD);
		}
		~Texture() {
			kl::cuda::free(buffer);
		}
	};
}
