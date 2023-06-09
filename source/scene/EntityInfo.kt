package scene

import editor.EditorWindow
import entity.Entity
import java.awt.GridLayout
import javax.swing.JPanel
import javax.swing.JTextField

class EntityInfo(private val editor: EditorWindow) : JPanel() {
    private val defaultNoneName = "-none-"

    private val entityName = JTextField(defaultNoneName, 25).let {
        it.horizontalAlignment = JTextField.CENTER
        it.isVisible = true
        it
    }

    init {
        layout = GridLayout(10, 1)
        add(entityName)
        isVisible = true
    }

    fun updateInfo() {
        editor.raytracer.scene.selectedEntity?.let { entity ->
            editor.raytracer.scene.getSlectedName()?.let {
                updateInfo(it, entity)
            }
        } ?: run {
            clearInfo()
        }
    }

    private fun clearInfo() {
        entityName.text = defaultNoneName
    }

    private fun updateInfo(name: String, entity: Entity) {
        entityName.text = name
    }
}
