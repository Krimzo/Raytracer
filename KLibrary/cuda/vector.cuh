#pragma once

#include "cuda/kcuda.cuh"


namespace kl::cuda {
	template<typename T> class vector {
	private:
		T* _buff = nullptr;
		size_t _size = 0;

	public:
		vector() {}
		vector(size_t size) {
			resize(size);
		}
		vector(const vector<T>& obj) : vector(obj._size) {
			kl::cuda::copy(_buff, obj._buff, _size, kl::cuda::transfer::DD);
		}
		vector& operator=(const vector& obj) {
			resize(obj.size());
			kl::cuda::copy(_buff, obj._buff, _size, kl::cuda::transfer::DD);
		}
		~vector() {
			clear();
		}

		T& operator[](size_t ind) {
			if (ind < _size) {
				return _buff[ind];
			}
			std::cout << "Cuda vector out of scope!" << std::endl;
			exit(69);
		}
		const T& operator[](size_t ind) const {
			if (ind < _size) {
				return _buff[ind];
			}
			std::cout << "Cuda vector out of scope!" << std::endl;
			exit(69);
		}

		T& front() {
			return operator[](0);
		}
		const T& front() const {
			return operator[](0);
		}
		T& back() {
			return operator[](_size - 1);
		}
		const T& back() const {
			return operator[](_size - 1);
		}

		T* pointer() const {
			return _buff;
		}
		size_t size() const {
			return _size;
		}

		bool resize(size_t newSize) {
			if (newSize != _size) {
				T* tempBuffer = nullptr;
				kl::cuda::alloc(tempBuffer, _size);
				kl::cuda::copy(tempBuffer, _buff, _size, kl::cuda::transfer::DD);
				kl::cuda::realloc(_buff, newSize);
				kl::cuda::copy(_buff, tempBuffer, std::min(_size, newSize), kl::cuda::transfer::DD);
				kl::cuda::free(tempBuffer);
				_size = newSize;
				return true;
			}
			return false;
		}
		void clear() {
			kl::cuda::free(_buff);
			_size = 0;
		}

		T& push_back(const T& obj) {
			resize(_size + 1);
			back() = obj;
			return back();
		}

		void toCPU(T* cpuBuff, size_t size = 0) const {
			kl::cuda::copy(cpuBuff, _buff, (size > 0 && size < _size) ? size : _size, kl::cuda::transfer::DH);
		}
		void fromCPU(const T* cpuBuff, size_t size = 0) {
			kl::cuda::copy(_buff, cpuBuff, (size > 0 && size < _size) ? size : _size, kl::cuda::transfer::HD);
		}

		void operator>>(T* cpuBuff) const {
			toCPU(cpuBuff);
		}
		void operator<<(const T* cpuBuff) {
			fromCPU(cpuBuff);
		}
	};
}
