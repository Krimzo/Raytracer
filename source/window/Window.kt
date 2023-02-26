package window

import java.awt.Dimension
import javax.swing.JFrame

class Window(width: Int, height: Int, title: String) : JFrame() {
    val target = DrawTarget()

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        this.size = Dimension(width, height)
        this.title = title

        add(target)
        isVisible = true
    }
}
