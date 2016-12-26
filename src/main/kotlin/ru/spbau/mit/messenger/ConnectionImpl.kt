package ru.spbau.mit.messenger

import ru.spbau.mit.data.Message
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class ConnectionImpl(private val socket: Socket): Connection {
    private val input: DataInputStream
    private val output: DataOutputStream


    init {
        input = DataInputStream(socket.inputStream)
        output = DataOutputStream(socket.outputStream)
    }
    override fun sendMessage(message: Message) {
        output.writeUTF(message.userName)
        output.writeUTF(message.text)
        output.flush()
    }

    override fun getMessage(): Message {
        val type = input.readInt()
        val userName = input.readUTF()
        val text = input.readUTF()
        if (type !in Message.MessageType.values().indices) {
            throw IllegalArgumentException("Unknown type $type of message")
        }

        return Message(Message.MessageType.values()[type], userName, text)
    }


    override fun disconnect() = socket.close()

    override fun isAlive(): Boolean = !socket.isClosed
}