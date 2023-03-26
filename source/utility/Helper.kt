package utility

fun safe(block: () -> Unit): Boolean {
    return try { block(); true }
    catch (ignored: Throwable) { false }
}
