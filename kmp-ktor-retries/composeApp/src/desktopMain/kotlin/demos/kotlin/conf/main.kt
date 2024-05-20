package demos.kotlin.conf

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        state = WindowState(width = 600.dp, height = 300.dp),
        onCloseRequest = ::exitApplication,
        title = "KMP Retries Client"
    ) {
        App()
    }
}