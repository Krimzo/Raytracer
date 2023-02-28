package logging

import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import javax.swing.table.DefaultTableCellRenderer


class LogWindow : JFrame(), MouseListener {
    private val logView = JTable(Logger).let {
        val centerRenderer = DefaultTableCellRenderer()
        centerRenderer.horizontalAlignment = JLabel.CENTER

        it.columnModel.getColumn(0).let {
            it.minWidth = 45; it.maxWidth = it.minWidth
            it.cellRenderer = centerRenderer
        }
        it.columnModel.getColumn(1).let {
            it.minWidth = 125; it.maxWidth = it.minWidth
            it.cellRenderer = centerRenderer
        }
        it.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        it.setDefaultEditor(Object::class.java, null)

        it.addMouseListener(this)
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
                    Logger.rowCount = 0
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
