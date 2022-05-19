#include "Raytracer.cuh"
#include "Kernels/Kernels.cuh"


void Raytracer::Start() {
	Raytracer::meshes.push_back(Raytracer::Mesh("resource/meshes/cube.obj"));
	Raytracer::meshes.push_back(Raytracer::Mesh("resource/meshes/pyramid.obj"));

	Raytracer::textures.push_back(Raytracer::Texture(kl::image(1, kl::colors::crimson)));
	Raytracer::textures.push_back(Raytracer::Texture(kl::image(1, kl::colors::wheat)));
	Raytracer::textures.push_back(Raytracer::Texture(kl::image("resource/textures/checkers.png")));
	Raytracer::textures.push_back(Raytracer::Texture(kl::image("resource/textures/dogo.png")));

	Raytracer::Entity wall(GetMesh(0), GetTexture(1));
	wall.position = kl::float3(2.0f, 0.0f, 0.0f);
	wall.scale = kl::float3(0.25f, 7.5f, 7.5f);
	Raytracer::entities.push_back(wall);

	Raytracer::Entity pyr1(GetMesh(0), GetTexture(2));
	pyr1.position = kl::float3(-2.0f, 0.25f, 0.0f);
	pyr1.scale = 2.0f;
	Raytracer::entities.push_back(pyr1);

	Raytracer::Entity pyr2(GetMesh(1), GetTexture(0));
	pyr2.position = kl::float3(-7.0f, 0.0f, -3.0f);
	pyr2.scale = 1.5f;
	pyr2.angular.y = -36.0f;
	Raytracer::entities.push_back(pyr2);
}
