package window

import java.awt.Dimension
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.JFrame

class Window(width: Int, height: Int, title: String) : JFrame(), ComponentListener {
    val target = DrawTarget()

    init {
        addComponentListener(this)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        this.size = Dimension(width, height)
        this.title = title

        add(target)
        isVisible = true
    }

    override fun componentResized(e: ComponentEvent?) {
        target.buffer = FrameBuffer(width, height)
    }

    override fun componentMoved(e: ComponentEvent?) {}

    override fun componentShown(e: ComponentEvent?) {}

    override fun componentHidden(e: ComponentEvent?) {}
}
