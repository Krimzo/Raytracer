#include "Raytracer.h"
#include "Kernels/Kernels.h"


void Raytracer::Start() {
	Raytracer::meshes.push_back(Raytracer::Mesh("resource/meshes/cube.obj"));
	Raytracer::meshes.push_back(Raytracer::Mesh("resource/meshes/pyramid.obj"));
	Raytracer::meshes.push_back(Raytracer::Mesh("resource/meshes/monke.obj"));

	Raytracer::textures.push_back(Raytracer::Texture(kl::image(1, kl::colors::lgray)));
	Raytracer::textures.push_back(Raytracer::Texture(kl::image(1, kl::colors::cyan)));
	Raytracer::textures.push_back(Raytracer::Texture(kl::image("resource/textures/checkers.png")));
	Raytracer::textures.push_back(Raytracer::Texture(kl::image("resource/textures/dogo.png")));

	Raytracer::Entity wall(&GetMesh(0), &GetTexture(0));
	wall.position = kl::float3(2.0f, 0.0f, 0.0f);
	wall.scale = kl::float3(0.25f, 7.5f, 10.0f);
	wall.rotation = kl::float3(0.0f, 0.0f, 30.0f);
	wall.roughness = 0.0f;
	Raytracer::entities.push_back(wall);

	wall.position.x += 0.025f;
	wall.scale.y *= 1.01f;
	wall.scale.z *= 1.01f;
	wall.roughness = 1.0f;
	Raytracer::entities.push_back(wall);

	Raytracer::Entity cube(&GetMesh(2), &GetTexture(2));
	cube.position = kl::float3(-4.0f, 0.25f, 0.0f);
	cube.scale = 2.0f;
	cube.rotation = kl::float3(0.0f, 45.0f, 0.0f);
	cube.roughness = 1.0f;
	Raytracer::entities.push_back(cube);

	Raytracer::Entity pyramid(&GetMesh(1), &GetTexture(1));
	pyramid.position = kl::float3(-8.0f, 0.0f, -3.0f);
	pyramid.scale = 1.5f;
	pyramid.angular.y = -36.0f;
	pyramid.roughness = 1.0f;
	Raytracer::entities.push_back(pyramid);
}
