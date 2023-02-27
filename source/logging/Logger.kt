package logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.DefaultListModel

object Logger : DefaultListModel<String>() {
    fun log(info: String) {
        addElement("${size() + 1}. [${getTimeInfo()}]: $info")
    }

    private fun getTimeInfo(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.now().format(formatter)
    }
}
