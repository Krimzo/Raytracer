#pragma once

#include <iostream>
#include <cuda_runtime.h>
#include <device_launch_parameters.h>

#define GLOBAL __global__
#define DEVICE __device__
#define HOST __host__

#define EXEC GLOBAL
#define GPU DEVICE
#define CPU HOST
#define ALL CPU GPU


namespace kl::cuda {
	template<typename T> inline void alloc(T*& buff, size_t count) {
		if (cudaMallocManaged(&buff, count * sizeof(T))) {
			std::cout << "Could not allocate " << (count * sizeof(T)) << " bytes of gpu memory!" << std::endl;
			exit(69);
		}
	}

	template<typename T> inline bool free(T*& buff) {
		if (buff) {
			cudaFree(buff);
			buff = nullptr;
			return true;
		}
		return false;
	}

	template<typename T> inline void realloc(T*& buff, size_t count) {
		kl::cuda::free(buff);
		kl::cuda::alloc(buff, count);
	}

	enum class transfer {
		HH = 0,
		HD = 1,
		DH = 2,
		DD = 3
	};
	template<typename T> inline bool copy(T* to, const void* from, size_t count, transfer type) {
		if (to && from && count) {
			cudaMemcpy(to, from, count * sizeof(T), cudaMemcpyKind(type));
			return true;
		}
		return false;
	}

	inline GPU size_t Index() {
		return size_t(blockIdx.x) * blockDim.x + threadIdx.x;
	}

	inline size_t execThreadsPerBlock = 128;
	template<typename T, typename... Args> inline void Exec(const T& kernel, const size_t& runs, const Args&... args) {
		kernel << <(runs / execThreadsPerBlock) + 1, execThreadsPerBlock >> > (runs, args...);
		cudaDeviceSynchronize();
	}
}
