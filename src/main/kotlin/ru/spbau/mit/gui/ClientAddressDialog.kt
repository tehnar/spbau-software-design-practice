package ru.spbau.mit.gui

import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class ClientAddressDialog: JPanel() {
    private val ipField = JTextField()
    private var portField = JTextField()

    init {
        add(JLabel("IP: "))
        add(ipField)
        add(JLabel("port: "))
        add(portField)
        ipField.columns = 15
        portField.columns = 5
    }

    fun ip() : String = ipField.text

    fun port() : Int = portField.text.toInt()
}