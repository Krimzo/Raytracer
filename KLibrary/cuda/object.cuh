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
		T& operator=(const T& obj) {
			*buff = obj;
		}
		T& operator=(const object& obj) {
			*buff = *obj.buff;
		}
		~object() {
			kl::cuda::free(buff);
		}

		operator T& () {
			return *buff;
		}
		operator const T& () const {
			return *buff;
		}
	};
}
