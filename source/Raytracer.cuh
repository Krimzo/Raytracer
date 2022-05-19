#pragma once

#include "Types/Entity.cuh"


namespace Raytracer {
	inline kl::window win;

	inline Raytracer::Texture pixelBuffer = kl::int2{ 1600, 900 };

	inline kl::camera camera;

	inline kl::timer timer;
	inline float deltaT = 0.0f;
	inline float elapsedT = 0.0f;

	inline std::list<kl::cuda::object<Raytracer::Mesh>> meshes;
	inline std::list<kl::cuda::object<Raytracer::Texture>> textures;
	inline kl::cuda::vector<Raytracer::Entity> entities;
	inline Raytracer::Mesh& GetMesh(size_t ind) {
		auto iter = meshes.begin();
		std::advance(iter, ind);
		return **iter;
	}
	inline Raytracer::Texture& GetTexture(size_t ind) {
		auto iter = textures.begin();
		std::advance(iter, ind);
		return **iter;
	}

	void SetupInput();
	void Start();
	void Update();
	void Resize(const kl::int2& newSize);
}
