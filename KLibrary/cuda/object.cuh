#pragma once

#include "cuda/kcuda.cuh"


namespace kl::cuda {
	template<typename T> class object {
	private:
		T* buff = nullptr;

	public:
		object() {
			kl::cuda::alloc(buff, 1);
		}
		object(const T& obj) : object() {
			operator=(obj);
		}
		object(const object& obj) : object() {
			operator=(obj);
		}
		void operator=(const T& obj) {
			*buff = obj;
		}
		void operator=(const object& obj) {
			*buff = *obj.buff;
		}
		~object() {
			kl::cuda::free(buff);
		}

		T& operator*() {
			return *buff;
		}
		const T& operator*() const {
			return *buff;
		}
		T* operator->() {
			return buff;
		}
		const T* operator->() const {
			return buff;
		}
	};
}
