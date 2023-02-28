import com.formdev.flatlaf.FlatDarculaLaf
import editor.EditorWindow
import entity.Entity
import entity.material.Material
import entity.material.Texture
import entity.mesh.StorageMesh
import math.vector.Vector3
import scene.Scene
import java.awt.Color

fun main() {
    FlatDarculaLaf.setup()
    val editorWindow = EditorWindow()

    setupTestScene(editorWindow.raytracer.scene)
    editorWindow.scenePanel.upateSceneView()
}

fun setupTestScene(scene: Scene) {
    // Meshes
    scene.meshes["Cube"] = StorageMesh("resource/meshes/cube.obj")
    scene.meshes["Monke"] = StorageMesh("resource/meshes/monke.obj")
    scene.meshes["Sphere"] = StorageMesh("resource/meshes/sphere.obj")

    // Textures
    scene.textures["Checkers"] = Texture("resource/textures/checkers.png")
    scene.textures["Dogo"] = Texture("resource/textures/dogo.png")

    // Materials
    val whiteScheckers = Material()
    whiteScheckers.color = Vector3(1.0)
    whiteScheckers.colorMap = scene.textures["Checkers"]
    scene.materials["WhiteCheckers"] = whiteScheckers

    val orangeScheckers = Material()
    orangeScheckers.color = Vector3(Color(220, 150, 105))
    orangeScheckers.colorMap = scene.textures["Checkers"]
    scene.materials["OrangeCheckers"] = orangeScheckers

    val greenScheckers = Material()
    greenScheckers.color = Vector3(Color(80, 200, 130))
    greenScheckers.colorMap = scene.textures["Checkers"]
    greenScheckers.roughness = 0.1
    scene.materials["GreenCheckers"] = greenScheckers

    // Cube
    val cube = Entity()
    cube.position = Vector3(-3.0, 0.0, 3.5)
    cube.rotation.y = 45.0
    cube.storageMesh = scene.meshes["Cube"]
    cube.material = scene.materials["WhiteCheckers"]
    scene["Cube"] = cube

    // Monke
    val monke = Entity()
    monke.position = Vector3(0.0, 0.1, 2.75)
    monke.rotation = Vector3(-10.0, -32.0, 0.0)
    monke.storageMesh = scene.meshes["Monke"]
    monke.material = scene.materials["OrangeCheckers"]
    scene["Monke"] = monke

    // Sphere
    val sphere = Entity()
    sphere.position = Vector3(3.0, 0.0, 3.0)
    sphere.storageMesh = scene.meshes["Sphere"]
    sphere.material = scene.materials["GreenCheckers"]
    scene["Sphere"] = sphere

    // Camera setup
    val camera = scene.camera
    camera.position = Vector3(-1.0, 2.0, -2.0)
    camera.direction = monke.position - camera.position

    // Light
    scene.directionalLight.direction = Vector3(-1.0, -1.0, 1.0)
}
