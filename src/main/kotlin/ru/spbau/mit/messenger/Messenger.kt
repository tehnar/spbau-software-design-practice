package ru.spbau.mit.messenger

import org.apache.logging.log4j.LogManager
import ru.spbau.mit.data.Message
import ru.spbau.mit.data.MessengerOptions
import java.io.IOException

/**
 * Class for exchanging messages with other user.
 * It works as server and client together: you can either connect to other client or wait until someone connects to you
 * After connection is established, server is shut down to prevent other users connecting to you.
 * Also, you cannot connect to other users until there is an active connection
 * Once connection has been aborted, server restarts and is ready to accept new connection
 */
class Messenger constructor(private val server: Server, private val client: Client,
                            private var options: MessengerOptions) {
    private var LOG = LogManager.getLogger(Messenger::class.java);

    private var onMessageReceived: (Message) -> Unit = {}
    private var onSystemMessage: (String) -> Unit = {}
    private var onError: (String) -> Unit = {}
    private var connection: Connection = NoConnection()
    private lateinit var processMessagesThread: Thread

    init {
        server.setOnNewConnection { newConnection(it) }
        startServer(options.serverPort)
    }

    fun getOptions() = options

    fun sendMessage(message: String) {
        try {
            connection.sendMessage(Message(Message.MessageType.TEXT_MESSAGE, options.userName, message))
        } catch (e: Exception) {
            onError("Failed to send message")
            LOG.error("Failed to send message", e)
        }
    }

    fun connectTo(ip: String, port: Int) {
        if (connection.isAlive()) {
            onSystemMessage("Connection to $ip:$port aborted: there is an active connection already")
            return
        }

        try {
            val clientConnection = client.connectTo(ip, port)
            newConnection(clientConnection)
        } catch (e: IOException) {
            onError("Unable to connect to $ip:$port")
            LOG.error("Unable to connect to $ip:$port", e)
        }
    }

    fun disconnect() {
        synchronized(this, {
            LOG.info("Disconnect")
            if (connection.isAlive()) {
                connection.disconnect()
            } else {
                onSystemMessage("You are already not connected")
            }
            connection = NoConnection()
        })
        processMessagesThread.join()
    }


    fun changeOptions(newOptions: MessengerOptions) {
        if (options.userName != newOptions.userName) {
            onSystemMessage("Nickname changed")
        }
        if (options.serverPort != newOptions.serverPort) {
            synchronized(this, {
                if (!connection.isAlive()) {
                    server.stop()
                    startServer(newOptions.serverPort)
                }
                onSystemMessage("Server port changed")
            })
        }

        options = newOptions
    }

    fun setOnMessageReceived(callback: (Message) -> Unit) {
        this.onMessageReceived = callback
    }


    fun setOnSystemMessage(callback: (String) -> Unit) {
        this.onSystemMessage = callback
    }

    fun setOnError(callback: (String) -> Unit) {
        this.onError = callback
    }

    private fun newConnection(newConnection: Connection) {
        LOG.info("New connection")
        synchronized(this, {
            if (!connection.isAlive()) {
                connection = newConnection
                onSystemMessage("Connection established")
                LOG.info("Connection established")
                processMessagesThread = Thread { processMessages() }
                processMessagesThread.start()
            } else {
                newConnection.disconnect()
                LOG.info("Connection declined: already has an active connection")
            }
        })
    }


    private fun processMessages() {
        while (connection.isAlive()) {
            try {
                onMessageReceived(connection.getMessage())
            } catch (e: Exception) {
                onSystemMessage("Connection lost")
                LOG.warn("Error while reading message: ", e)
                break
            }
        }

        connection.disconnect()
        LOG.info("Restarting server")
        server.stop()
        startServer(options.serverPort)
    }

    private fun startServer(port: Int) {
        try {
            server.start(port)
        } catch (e: IOException) {
            onError("Cannot start server on port $port")
            LOG.error("Cannot start server on port $port", e)
        }
    }

    /**
     * Utility class that represents non-existing connection (to avoid dealing with nullable types)
     */
    private class NoConnection: Connection {
        override fun sendMessage(message: Message) {
            throw IllegalStateException("Attempt to send message via non-existing connection")
        }

        override fun getMessage(): Message {
            while (true) {
            }
        }

        override fun disconnect() {
        }

        override fun isAlive() = false
    }
}