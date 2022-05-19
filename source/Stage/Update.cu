#include "Raytracer.cuh"
#include "Kernels/Kernels.cuh"


void Raytracer::Update() {
	// Time
	deltaT = timer.interval();
	elapsedT = timer.elapsed();

	// Phys
	for (size_t i = 0; i < Raytracer::entities.size(); i++) {
		Raytracer::entities[i].updatePhys(Raytracer::deltaT);
	}

	// Precompute vertices
	kl::cuda::Exec(Kernels::Precompute, Raytracer::entities.size(), Raytracer::entities.pointer());

	// Trace
	kl::cuda::Exec(Kernels::Raytrace, pixelBuffer.len, pixelBuffer.buffer, pixelBuffer.size,
		Raytracer::camera.position, Raytracer::camera.matrix().inverse(),
		Raytracer::entities.pointer(), Raytracer::entities.size());

	// Draw texture
	static kl::image tempPixelBuffer(pixelBuffer.size);
	tempPixelBuffer.resize(pixelBuffer.size);
	cudaMemcpy(tempPixelBuffer.pointer(), pixelBuffer.buffer, pixelBuffer.len * sizeof(kl::color), cudaMemcpyDeviceToHost);
	win.drawImage(tempPixelBuffer);

	// FPS
	win.setTitle(std::to_string(int(1.0f / deltaT)));
}
