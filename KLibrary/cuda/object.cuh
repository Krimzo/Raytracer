#pragma once

#include "cuda/kcuda.cuh"


namespace kl::cuda {
	template<typename T> class object {
	private:
		T* val = nullptr;

	public:
		object() {
			if (cudaMallocManaged(&val, sizeof(T))) {
				std::cout << "Could not allocate " << sizeof(T) << " bytes of gpu memory!" << std::endl;
				exit(69);
			}
		}
		object(const T& obj) : object() {
			operator=(obj);
		}
		object(const object& obj) : object() {
			operator=(obj);
		}
		T& operator=(const T& obj) {
			*val = obj;
		}
		T& operator=(const object& obj) {
			*val = *obj.val;
		}
		~object() {
			cudaFree(val);
			val = nullptr;
		}

		operator T* () const {
			return val;
		}
		operator T& () const {
			return *val;
		}
	};
}
