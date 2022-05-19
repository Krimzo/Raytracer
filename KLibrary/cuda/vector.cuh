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
		vector(const vector<T>& obj) : vector(obj.size()) {
			if (_buff) {
				cudaMemcpy(_buff, obj.pointer(), _size * sizeof(T), cudaMemcpyDeviceToDevice);
			}
		}
		vector& operator=(const vector& obj) {
			resize(obj.size());
			if (_buff) {
				cudaMemcpy(_buff, obj.pointer(), _size * sizeof(T), cudaMemcpyDeviceToDevice);
			}
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

		bool resize(size_t size) {
			if (size != _size) {
				vector<T> copy = *this;
				clear();
				cudaMallocManaged(&_buff, size * sizeof(T));
				if (_buff) {
					_size = size;
					cudaMemcpy(_buff, copy.pointer(), copy.size() * sizeof(T), cudaMemcpyDeviceToDevice);
					return true;
				}
			}
			return false;
		}
		void clear() {
			if (_buff) {
				cudaFree(_buff);
				_buff = nullptr;
				_size = 0;
			}
		}

		T& push_back(const T& obj) {
			resize(_size + 1);
			back() = obj;
			return back();
		}

		void toCPU(T* cpuBuff, size_t size = 0) const {
			if (_buff && cpuBuff) {
				cudaMemcpy(cpuBuff, _buff, ((size > 0 && size <= _size) ? size : _size) * sizeof(T), cudaMemcpyDeviceToHost);
			}
		}
		void fromCPU(const T* cpuBuff, size_t size = 0) {
			if (_buff && cpuBuff) {
				cudaMemcpy(_buff, cpuBuff, ((size > 0 && size <= _size) ? size : _size) * sizeof(T), cudaMemcpyHostToDevice);
			}
		}

		void operator>>(T* cpuBuff) const {
			toCPU(cpuBuff);
		}
		void operator<<(const T* cpuBuff) {
			fromCPU(cpuBuff);
		}
	};
}
