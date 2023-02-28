package logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.table.DefaultTableModel

object Logger : DefaultTableModel() {
    init {
        addColumn("Index")
        addColumn("Time")
        addColumn("Info")
    }

    fun log(info: String) {
        addRow(arrayOf("${rowCount + 1}.", "[${getTimeInfo()}]", info))
    }

    private fun getTimeInfo(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.now().format(formatter)
    }
}
