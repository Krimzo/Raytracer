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
	inline GPU size_t Index() {
		return size_t(blockIdx.x) * blockDim.x + threadIdx.x;
	}

	inline size_t execThreadsPerBlock = 128;
	template<typename T, typename... Args> inline void Exec(const T& kernel, const size_t& runs, const Args&... args) {
		kernel << <(runs / execThreadsPerBlock) + 1, execThreadsPerBlock >> > (runs, args...);
		cudaDeviceSynchronize();
	}
}
