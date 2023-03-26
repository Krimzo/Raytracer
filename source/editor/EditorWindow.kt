package editor

import logging.LogPanel
import raytracer.Raytracer
import render.RenderPanel
import scene.ScenePanel
import utility.safe
import java.awt.BorderLayout
import java.awt.Dimension
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JSplitPane

class EditorWindow : JFrame() {
    val scenePanel = ScenePanel(this)
    val renderPanel = RenderPanel(this)
    val logPanel = LogPanel(this)

    val raytracer = Raytracer(this)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        this.size = Dimension(1600, 900)
        this.title = "Krimz Raytracer"

        layout = BorderLayout()
        JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scenePanel, renderPanel).let { panel0 ->
            panel0.isOneTouchExpandable = true
            panel0.isVisible = true

            JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel0, logPanel).let { panel1 ->
                panel1.isOneTouchExpandable = true
                panel1.resizeWeight = 0.95
                panel1.isVisible = true

                add(panel1, BorderLayout.CENTER)
            }
        }

        safe {
            iconImage = ImageIO.read(File("resource/icons/application.png"))
        }

        isVisible = true
        extendedState = (extendedState or MAXIMIZED_BOTH)
    }
}
