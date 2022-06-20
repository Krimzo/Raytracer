#pragma once

#include "Types/Entity.h"


namespace Raytracer {
	inline kl::window win;

	inline Raytracer::Texture pixelBuffer = kl::int2{ 1600, 900 };

	inline kl::camera camera;
	inline kl::float3 sunDir = { 1.0f, -0.25f, 0.0f };

	inline std::list<kl::cuda::object<Raytracer::Mesh>> meshes;
	inline std::list<kl::cuda::object<Raytracer::Texture>> textures;
	inline kl::cuda::vector<Raytracer::Entity> entities;
	inline Raytracer::Mesh& GetMesh(uint64 ind) {
		auto iter = meshes.begin();
		std::advance(iter, ind);
		return **iter;
	}
	inline Raytracer::Texture& GetTexture(uint64 ind) {
		auto iter = textures.begin();
		std::advance(iter, ind);
		return **iter;
	}

	inline kl::timer timer;
	inline float deltaT = 0.0f;
	inline float elapsedT = 0.0f;

	void SetupInput();
	void Start();
	void User();
	void Update();
	void Resize(const kl::int2& newSize);

	namespace Debug {
		inline std::list<std::string> messages;
		inline std::unordered_map<std::string, float> times;
		inline float startTime = 0.0f;
		inline void TimeItStart() {
			startTime = float(timer.elapsed());
		}
		inline void TimeItEnd(const std::string& message) {
			if (times.find(message) == times.end()) {
				messages.push_back(message);
			}
			times[message] = float(timer.elapsed() - startTime);
		}
		inline void DisplayTimes() {
			std::stringstream ss;
			ss << std::fixed << std::setprecision(2);
			for (auto& mes : messages) {
				ss << "[" << mes << " " << (times[mes] * 1e3f) << "] ";
			}
			ss << "(FPS " << int(1.0f / deltaT) << ") ";
			win.title(ss.str());
		}
	}
}
