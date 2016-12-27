package ru.spbau.mit.gui

import ru.spbau.mit.data.Message
import ru.spbau.mit.messenger.Messenger
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

class ChatView(size: Dimension, private val messenger: Messenger): JFrame("Chat") {
    private val chatArea = JTextPane()
    private val typingStatus = JLabel()

    private val messageField = JTextField()
    private val sendButton = JButton("Send")

    private val nickNameStyle = SimpleAttributeSet()
    private val messageStyle = SimpleAttributeSet()
    private val systemMessageStyle = SimpleAttributeSet()
    private val errorMessageStyle = SimpleAttributeSet()

    private val typingTimer: Timer
    private var lastTypingTime: Long = System.currentTimeMillis()
    private val TYPING_THRESHOLD = 1000

    init {
        this.size = size
        initMenu()
        initChatArea()
        initMessageArea()
        initCallbacks()
        initStyles()
        typingTimer = Timer(TYPING_THRESHOLD) {
            if (!messenger.isConnectionAlive()) {
                return@Timer
            }
            if (lastTypingTime + TYPING_THRESHOLD > System.currentTimeMillis()) {
                messenger.sendTypingStatus(true)
            } else {
                messenger.sendTypingStatus(false)
            }
        }
        typingTimer.start()
    }

    private fun initMenu() {
        val menu = JMenu("Menu")
        JMenuItem("Connect").let {
            it.addActionListener( { connect() })
            menu.add(it)
        }

        JMenuItem("Disconnect").let {
            it.addActionListener( { disconnect() })
            menu.add(it)
        }

        JMenuItem("Settings").let {
            it.addActionListener( { settings() })
            menu.add(it)
        }

        JMenuBar().let {
            it.add(menu)
            jMenuBar = it
        }
    }

    private fun initChatArea() {
        JScrollPane(chatArea).let {
            it.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
            add(it, BorderLayout.CENTER)
        }
        chatArea.isEditable = false
    }

    private fun initMessageArea() {
        val messagePanel = JPanel().let {
            it.add(messageField)
            it.add(sendButton)
            return@let it
        }
        typingStatus.horizontalAlignment = SwingConstants.LEFT
        JPanel().let {
            it.layout = BoxLayout(it, BoxLayout.Y_AXIS)
            it.add(typingStatus)
            it.add(messagePanel)
            add(it, BorderLayout.SOUTH)
        }

        messageField.addActionListener {
            messenger.sendMessage(messageField.text)
            addMessage(Message(Message.MessageType.TEXT_MESSAGE, "You", messageField.text))
            messageField.text = ""
        }

        messageField.addKeyListener(object: KeyListener {
            override fun keyTyped(p0: KeyEvent?) {
                lastTypingTime = System.currentTimeMillis()
                if (!typingTimer.isRunning) {
                    typingTimer.restart()
                }
            }

            override fun keyPressed(p0: KeyEvent?) {
                lastTypingTime = System.currentTimeMillis()
                if (!typingTimer.isRunning) {
                    typingTimer.restart()
                }
            }

            override fun keyReleased(p0: KeyEvent?) {
                lastTypingTime = System.currentTimeMillis()
                if (!typingTimer.isRunning) {
                    typingTimer.restart()
                }
            }
        })

        messageField.preferredSize = Dimension(size.width - 150, 25)
        sendButton.preferredSize = Dimension(100, 25)
        sendButton.addActionListener {
            messenger.sendMessage(messageField.text)
            messageField.text = ""
        }
    }

    private fun initCallbacks() {
        messenger.setOnError { errorMessage(it) }
        messenger.setOnSystemMessage {  systemMessage(it) }
        messenger.setOnMessageReceived { addMessage(it) }
    }

    private fun initStyles() {
        StyleConstants.setForeground(nickNameStyle, Color.BLUE)
        StyleConstants.setForeground(messageStyle, Color.BLACK)
        StyleConstants.setForeground(systemMessageStyle, Color.GRAY)
        StyleConstants.setForeground(errorMessageStyle, Color.RED)

        StyleConstants.setFontSize(nickNameStyle, 12)
        StyleConstants.setFontSize(messageStyle, 12)
        StyleConstants.setFontSize(systemMessageStyle, 12)
        StyleConstants.setFontSize(errorMessageStyle, 12)
    }

    private fun connect() {
        val panel = ClientAddressDialog()
        val result = JOptionPane.showConfirmDialog(null, panel, "Enter client address", JOptionPane.OK_CANCEL_OPTION)
        if (result == JOptionPane.OK_OPTION) {
            messenger.connectTo(panel.ip(), panel.port())
        }
    }

    private fun disconnect() {
        messenger.disconnect()
    }

    private fun settings() {
        val panel = SettingsDialog(messenger.getOptions())
        val result = JOptionPane.showConfirmDialog(null, panel, "Enter client address", JOptionPane.OK_CANCEL_OPTION)
        if (result == JOptionPane.OK_OPTION) {
            messenger.changeOptions(panel.options())
        }
    }

    private fun addMessage(message: Message) {
        SwingUtilities.invokeLater {
            chatArea.styledDocument.let {
                when (message.type) {
                    Message.MessageType.TEXT_MESSAGE -> {
                        it.insertString(it.length, message.userName + ": ", nickNameStyle)
                        it.insertString(it.length, message.text + "\n", messageStyle)
                    }
                    Message.MessageType.START_WRITING -> {
                        typingStatus.text = "${message.userName} is typing..."
                    }
                    Message.MessageType.STOP_WRITING -> {
                        typingStatus.text = ""
                    }
                }
            }
        }
    }

    private fun systemMessage(message: String) {
        SwingUtilities.invokeLater {
            chatArea.styledDocument.let {
                it.insertString(it.length, message + "\n", systemMessageStyle)
            }
        }
    }

    private fun errorMessage(message: String) {
        SwingUtilities.invokeLater {
            chatArea.styledDocument.let {
                it.insertString(it.length, message + "\n", errorMessageStyle)
            }
        }
    }
}