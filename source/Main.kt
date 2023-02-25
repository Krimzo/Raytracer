import entity.Entity
import entity.material.Material
import entity.material.Texture
import entity.mesh.Mesh
import math.vector.Float3
import math.vector.Int2
import raytracer.Raytracer
import window.FrameBuffer
import window.Window
import java.awt.Color
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

fun main() {
    Main()
}

class Main : KeyListener {
    private val window = Window(Int2(1600, 900), "Raytracer")
    private var buffer = FrameBuffer(window.frameSize)
    private val tracer = Raytracer()

    init {
        setupTestScene()
        window.addKeyListener(this)
        while (window.process()) {
            window.display(buffer)
            Thread.sleep(16)
        }
    }

    private fun setupTestScene() {
        // Monke
        val monke = Entity()
        monke.position.z = -5f
        monke.rotation.y = 32f
        monke.rotation.x = 10f
        monke.mesh = Mesh("resource/meshes/monke.obj")
        monke.material = Material()
        monke.material?.color = Float3(Color(220, 150, 105))
        monke.material?.colorMap = Texture("resource/textures/checkers.png")
        tracer.scene["Monke"] = monke

        // Light setup
        tracer.scene.ambientLight.color = Float3(1f)
        tracer.scene.directionalLight.direction = Float3(-1f, -1f, -1f)
    }

    private fun renderScene() {
        // Check
        if (!window.isResizable) { return }
        window.isResizable = false

        // Render
        buffer = FrameBuffer(window.frameSize)
        val msRenderTime = tracer.render(buffer)

        // Finalize
        if (msRenderTime < 1e3) {
            window.title = "Raytracer [$msRenderTime ms]"
        }
        else {
            window.title = "Raytracer [${msRenderTime / 1e3} s]"
        }
        window.isResizable = true
    }

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent?) {}

    override fun keyReleased(e: KeyEvent?) {
        when (e?.keyChar) {
            'r' -> { renderScene() }
            's' -> { buffer.saveToFile("render.png") }
        }
    }
}
