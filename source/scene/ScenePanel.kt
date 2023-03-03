package scene

import editor.EditorWindow
import entity.Entity
import logging.Logger
import utility.Timer
import utility.pathWithChangedExtension
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import java.util.*
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter

class ScenePanel(private val editor: EditorWindow) : JPanel(), MouseListener {
    private val renderSaveDirectiory = "renders"

    private val newSceneButton = JButton("New Scene").let {
        it.addActionListener {
            editor.raytracer.scene = Scene()
            upateSceneView()
        }
        it.isVisible = true
        it
    }

    private val renderSceneButton = JButton("Render Scene").let {
        it.addActionListener {
            Thread { renderScene() }.start()
        }
        it.isVisible = true
        it
    }

    private val saveSceneButton = JButton("Save Scene").let {
        it.addActionListener {
            val chooser = JFileChooser(".")
            chooser.fileFilter = FileNameExtensionFilter("Scene files [.scene]", "scene")
            chooser.isAcceptAllFileFilterUsed = false

            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                val filepath = chooser.selectedFile.pathWithChangedExtension("scene")
                editor.raytracer.scene.saveToFile(filepath)
            }
        }
        it.isVisible = true
        it
    }

    private val loadSceneButton = JButton("Load Scene").let {
        it.addActionListener {
            val chooser = JFileChooser(".")
            chooser.fileFilter = FileNameExtensionFilter("Scene files [.scene]", "scene")
            chooser.isAcceptAllFileFilterUsed = false

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                val filepath = chooser.selectedFile.absolutePath
                editor.raytracer.scene = Scene.loadFromFile(filepath)
                upateSceneView()
            }
        }
        it.isVisible = true
        it
    }

    private val sceneViewModel = DefaultListModel<String>()

    private val sceneView = JList(sceneViewModel).let {
        val renderer = it.cellRenderer as DefaultListCellRenderer
        renderer.horizontalAlignment = SwingConstants.CENTER

        it.addMouseListener(this)
        it.isVisible = true
        it
    }

    init {
        layout = BorderLayout()
        JPanel().let { panel0 ->
            panel0.layout = BorderLayout()
            panel0.add(newSceneButton, BorderLayout.WEST)
            panel0.add(renderSceneButton, BorderLayout.CENTER)

            JPanel().let { panel1 ->
                panel1.layout = BorderLayout()
                panel1.add(saveSceneButton, BorderLayout.WEST)
                panel1.add(loadSceneButton, BorderLayout.EAST)

                panel1.isVisible = true
                panel0.add(panel1, BorderLayout.EAST)
            }

            panel0.isVisible = true
            add(panel0, BorderLayout.NORTH)
        }

        add(sceneView, BorderLayout.CENTER)
        isVisible = true
    }

    fun upateSceneView() {
        sceneViewModel.clear()
        sceneViewModel.addAll(editor.raytracer.scene.keys)
    }

    private fun saveRenderedImage(): String {
        var nameCounter = 0
        var chosenFileName: String
        do {
            chosenFileName = "${renderSaveDirectiory}/render_${++nameCounter}.png"
        }
        while (File(chosenFileName).exists())

        File(renderSaveDirectiory).mkdirs()
        editor.renderPanel.buffer.saveAsPNG(chosenFileName)
        return chosenFileName
    }

    @Synchronized private fun renderScene() {
        newSceneButton.isEnabled = false
        renderSceneButton.isEnabled = false
        saveSceneButton.isEnabled = false
        loadSceneButton.isEnabled = false

        Logger.log("Render started")
        val timer = Timer()
        editor.raytracer.render()
        val elapsedTime = timer.elapsed()
        Logger.log("Render finished in [%.2f] seconds".format(Locale.US, elapsedTime))

        val savedImagePath = saveRenderedImage()
        Logger.log("Saved the render ($savedImagePath)")

        loadSceneButton.isEnabled = true
        saveSceneButton.isEnabled = true
        renderSceneButton.isEnabled = true
        newSceneButton.isEnabled = true
    }

    override fun mouseClicked(e: MouseEvent?) {
        if (e == null) { return }

        if (SwingUtilities.isRightMouseButton(e)) {
            val menu = JPopupMenu()
            val closer = JMenuItem(object : AbstractAction("New Entity") {
                override fun actionPerformed(e: ActionEvent) {
                    var counter = 0
                    var newName = "New Entity"
                    while (editor.raytracer.scene.containsKey(newName)) {
                        newName = "New Entity ${counter++}"
                    }

                    editor.raytracer.scene[newName] = Entity()
                    upateSceneView()
                }
            })
            menu.add(closer)
            menu.show(this, e.x, e.y)
        }
    }

    override fun mousePressed(e: MouseEvent?) {}

    override fun mouseReleased(e: MouseEvent?) {}

    override fun mouseEntered(e: MouseEvent?) {}

    override fun mouseExited(e: MouseEvent?) {}
}
