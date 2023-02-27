package logging

import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*


class LogWindow : JFrame(), MouseListener {
    private val logView = JList(Logger).let {
        it.addMouseListener(this)
        it.layoutOrientation = JList.VERTICAL
        it.isVisible = true
        it
    }

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(500, 500)
        title = "Log View"

        add(JScrollPane(logView))

        isVisible = true
    }

    override fun mouseClicked(e: MouseEvent?) {
        if (e == null) { return }

        if (SwingUtilities.isRightMouseButton(e)) {
            val menu = JPopupMenu()
            val closer = JMenuItem(object : AbstractAction("Clear") {
                override fun actionPerformed(e: ActionEvent) {
                    Logger.clear()
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
