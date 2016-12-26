package ru.spbau.mit

import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import ru.spbau.mit.data.Message
import ru.spbau.mit.data.MessengerOptions
import ru.spbau.mit.messenger.Client
import ru.spbau.mit.messenger.Connection
import ru.spbau.mit.messenger.Messenger
import ru.spbau.mit.messenger.Server
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestMessenger {
    private val server = mock<Server>()
    private val client = mock<Client>()
    private val PORT = 1111
    private val IP = "ip"
    private val NICK = "TestNick"

    @Test
    fun testMessageSending() {
        val messenger = Messenger(server, client, MessengerOptions(NICK, PORT))
        val messages = listOf(
                Message(Message.MessageType.TEXT_MESSAGE, NICK, "Hi"),
                Message(Message.MessageType.TEXT_MESSAGE, NICK, "Hello"),
                Message(Message.MessageType.TEXT_MESSAGE, NICK, "Bye")
        )
        val connection = ConnectionDummy(mutableListOf())

        whenever(client.connectTo(IP, PORT)).thenReturn(connection)

        messenger.setOnMessageReceived { assertTrue(false) }
        messenger.connectTo(IP, PORT)
        messages.forEach { messenger.sendMessage(it.text) }

        verify(server, times(1)).start(PORT)
        connection.disconnect()
        Thread.sleep(1000)
        verify(client, times(1)).connectTo(IP, PORT)
        verify(server, times(2)).start(PORT)
        verify(server, times(1)).stop()

        assertEquals(messages, connection.sentMessages)
    }

    @Test
    fun testMessageReceiving() {
        val messenger = Messenger(server, client, MessengerOptions(NICK, PORT))
        val messages = listOf(
                Message(Message.MessageType.TEXT_MESSAGE, "Other", "Hi"),
                Message(Message.MessageType.TEXT_MESSAGE, "Other", "Hello"),
                Message(Message.MessageType.TEXT_MESSAGE, "Other", "Bye")
        )
        val connection = ConnectionDummy(messages.toMutableList())

        whenever(client.connectTo(IP, PORT)).thenReturn(connection)

        val receivedMessages = mutableListOf<Message>()
        messenger.setOnMessageReceived { receivedMessages.add(it) }
        messenger.connectTo(IP, PORT)
        Thread.sleep(1000)
        verify(server, times(1)).start(PORT)
        connection.disconnect()
        Thread.sleep(1000)
        verify(client, times(1)).connectTo(IP, PORT)
        verify(server, times(2)).start(PORT)
        verify(server, times(1)).stop()

        assertEquals(messages, receivedMessages)
    }

    @Test
    fun testStartStopServer() {
        val connection = ConnectionDummy(mutableListOf())
        val messenger = Messenger(server, client, MessengerOptions(NICK, PORT))
        val ITER = 1000

        whenever(client.connectTo(IP, PORT)).thenReturn(connection)
        for (i in 0..ITER) {
            messenger.connectTo(IP, PORT)
            messenger.disconnect()
        }

        val order = inOrder(server)
        order.verify(server).start(PORT)
        for (i in 0..ITER) {
            order.verify(server).stop()
            order.verify(server).start(PORT)
        }
    }

    @Test
    fun testChangeServerPort() {
        var callback: (Connection) -> Unit = { }
        whenever(server.setOnNewConnection(any())).then { callback = it.arguments[0] as (Connection) -> Unit; null}
        val messenger = Messenger(server, client, MessengerOptions(NICK, PORT))
        val ITER = 1000
        val connection = ConnectionDummy(mutableListOf())
        whenever(client.connectTo(IP, PORT)).thenReturn(connection)
        for (i in 0..ITER) {
            messenger.changeOptions(MessengerOptions(NICK, PORT + i))
            callback(connection)
            messenger.disconnect()
        }

        val order = inOrder(server)
        order.verify(server).start(PORT)
        for (i in 0..ITER) {
            order.verify(server).stop()
            order.verify(server).start(PORT + i)
        }
    }

    private class ConnectionDummy(val messageQueue: MutableList<Message>) : Connection {
        val sentMessages = mutableListOf<Message>()
        var alive = true

        override fun sendMessage(message: Message) {
            sentMessages.add(message)
        }

        override fun getMessage(): Message {
            if (messageQueue.isNotEmpty()) {
                val message = messageQueue[0]
                messageQueue.removeAt(0)
                return message
            }

            while (true) {
                synchronized(this, {
                    if (!alive) {
                        throw IOException("Disconnected")
                    }
                })
            }
        }
        
        override fun disconnect() {
            synchronized(this, {
                alive = false
            })
        }

        override fun isAlive(): Boolean = alive

    }
}