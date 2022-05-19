#pragma once

#include <iostream>
#include <fstream>
#include <sstream>
#include "KrimzLib.cuh"


inline std::vector<kl::vertex> ParseObjectFile(const String& filePath, bool flipZ) {
	// Temp vertex buffer
	std::vector<kl::vertex> vertexData;

	// Opening the file
	std::fstream fileStream;
	fileStream.open(filePath, std::ios::in);
	if (!fileStream.is_open()) {
		std::cout << "Could not open file \"" << filePath << "\"!" << std::endl;
		exit(69);
	}

	// Temp load buffers
	std::vector<kl::float3> xyzBuffer;
	std::vector<kl::float2> uvBuffer;
	std::vector<kl::float3> normBuffer;

	// Z flipper
	const int zFlip = flipZ ? -1 : 1;

	// Parsing data
	for (String fileLine; std::getline(fileStream, fileLine);) {
		// Splitting the string by spaces
		std::vector<String> lineParts;
		std::stringstream lineStream(fileLine);
		for (String linePart; std::getline(lineStream, linePart, ' ');) {
			lineParts.push_back(linePart);
		}

		// Parsing the data
		if (lineParts[0] == "v") {
			xyzBuffer.push_back(kl::float3(std::stof(lineParts[1]), std::stof(lineParts[2]), zFlip * std::stof(lineParts[3])));
		}
		else if (lineParts[0] == "vt") {
			uvBuffer.push_back(kl::float2(std::stof(lineParts[1]), std::stof(lineParts[2])));
		}
		else if (lineParts[0] == "vn") {
			normBuffer.push_back(kl::float3(std::stof(lineParts[1]), std::stof(lineParts[2]), zFlip * std::stof(lineParts[3])));
		}
		else if (lineParts[0] == "f") {
			for (int i = 1; i < 4; i++) {
				// Getting the world, texture and normal indexes
				std::vector<String> linePartParts;
				std::stringstream linePartStream(lineParts[i]);
				for (String linePartPart; std::getline(linePartStream, linePartPart, '/');) {
					linePartParts.push_back(linePartPart);
				}

				// Saving the data
				vertexData.push_back(
					kl::vertex(
						xyzBuffer[std::stoi(linePartParts[0]) - 1],
						uvBuffer[std::stoi(linePartParts[1]) - 1],
						normBuffer[std::stoi(linePartParts[2]) - 1]
					)
				);
			}
		}
	}

	// Closing the file
	fileStream.close();

	// Data return
	return vertexData;
}

namespace Raytracer {
	struct Mesh {
		size_t size = 0;
		kl::triangle* buffer = nullptr;
		kl::float3 far = 0.0f;

		Mesh(const std::vector<kl::vertex>& vertices) : size(vertices.size() / 3) {
			kl::cuda::alloc(buffer, size);
			kl::cuda::copy(buffer, &vertices[0], size, kl::cuda::transfer::HD);
			for (auto& vert : vertices) {
				if (vert.world.length() > far.length()) {
					far = vert.world;
				}
			}
		}
		Mesh(const String& filePath) : Mesh(ParseObjectFile(filePath, true)) {}
		Mesh(const Mesh& obj) : size(obj.size), far(obj.far) {
			kl::cuda::alloc(buffer, obj.size);
			kl::cuda::copy(buffer, obj.buffer, obj.size, kl::cuda::transfer::DD);
		}
		void operator=(const Mesh& obj) {
			size = obj.size;
			far = obj.far;
			kl::cuda::realloc(buffer, obj.size);
			kl::cuda::copy(buffer, obj.buffer, obj.size, kl::cuda::transfer::DD);
		}
		~Mesh() {
			kl::cuda::free(buffer);
		}
	};
}
