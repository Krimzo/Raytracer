package gui

import math.vector.Vector3
import javax.swing.JPanel
import javax.swing.JTextField

class EditVector3 : JPanel() {
    var vector: Vector3? = null

    init {
        add(JTextField(""))
        isVisible = true
    }
}
