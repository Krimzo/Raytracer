import entity.Entity
import entity.material.Material
import entity.material.Texture
import entity.mesh.Mesh
import math.vector.Vector3
import raytracer.Raytracer
import window.Timer
import window.Window
import java.awt.Color
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

fun main() {
    Main()
}

class Main : KeyListener {
    private val window = Window(1600, 900, "Raytracer")
    private val tracer = Raytracer()

    init {
        setupTestScene()
        window.addKeyListener(this)
    }

    private fun setupTestScene() {
        // Monke
        val monke = Entity()
        monke.position.z = 3.0
        monke.rotation.y = -32.0
        monke.rotation.x = -10.0
        monke.mesh = Mesh("resource/meshes/monke.obj")
        monke.material = Material()
        monke.material?.color = Vector3(Color(220, 150, 105))
        monke.material?.colorMap = Texture("resource/textures/checkers.png")
        tracer.scene["Monke"] = monke

        // Light setup
        tracer.scene.directionalLight.direction = Vector3(-1.0, -1.0, 1.0)
    }

    @Synchronized private fun renderScene() {
        val timer = Timer()
        window.isResizable = false
        tracer.render(window)
        window.isResizable = true
        window.title = "Raytracer [${timer.elapsed()} s]"
    }

    @Synchronized private fun saveScene() {
        window.target.buffer.saveToFile("render.png")
    }

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent?) {}

    override fun keyReleased(e: KeyEvent?) {
        when (e?.keyChar) {
            'r' -> { Thread { renderScene() }.start() }
            's' -> { Thread { saveScene() }.start() }
        }
    }
}