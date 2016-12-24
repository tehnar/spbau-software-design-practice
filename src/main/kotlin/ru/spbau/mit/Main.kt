package ru.spbau.mit

import ru.spbau.mit.data.MessengerOptions
import ru.spbau.mit.gui.ChatView
import ru.spbau.mit.messenger.*
import java.awt.Dimension
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    val view = ChatView(Dimension(800, 600), Messenger(ServerImpl(), ClientImpl(), MessengerOptions("Tehnar", 12346)))
    view.isResizable = false
    view.isVisible = true
    view.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
}