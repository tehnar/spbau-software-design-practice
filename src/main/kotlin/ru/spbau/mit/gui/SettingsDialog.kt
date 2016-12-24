package ru.spbau.mit.gui

import ru.spbau.mit.data.MessengerOptions
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class SettingsDialog constructor(oldOptions: MessengerOptions): JPanel() {
    private val nickNameField = JTextField()
    private val serverPortField = JTextField()

    init {
        JPanel().let {
            it.add(JLabel("Nickname: "))
            it.add(nickNameField)
            add(it)
        }

        JPanel().let {
            add(JLabel("Server port: "))
            add(serverPortField)
        }

        nickNameField.text = oldOptions.userName
        serverPortField.text = oldOptions.serverPort.toString()

        nickNameField.columns = 15
        serverPortField.columns = 5
    }

    fun options() = MessengerOptions(nickNameField.text, serverPortField.text.toInt())
}