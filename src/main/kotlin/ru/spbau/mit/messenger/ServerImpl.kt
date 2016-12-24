package ru.spbau.mit.messenger

import org.apache.logging.log4j.LogManager
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.SocketException

class ServerImpl: Server {
    private var LOG = LogManager.getLogger(ServerImpl::class.java);

    private var onNewConnection: (Connection) -> Unit = {}
    private lateinit var server: ServerSocket
    private lateinit var serverThread: Thread

    override fun start(port: Int) {
        server = ServerSocket()
        server.reuseAddress = true
        server.bind(InetSocketAddress(port))

        serverThread = Thread {
            LOG.info("Server started on port ${server.localPort}")
            while(true) {
                try {
                    onNewConnection(ConnectionImpl(server.accept()))
                    server.close()
                    LOG.info("Server closed")
                    return@Thread
                } catch (e: SocketException) {
                    LOG.info("Server closed", e)
                    server.close()
                    return@Thread
                } catch (e: Exception) {
                    LOG.error("Error while waiting for client ", e)
                    server.close()
                    return@Thread
                }
            }
        }

        serverThread.start()
    }

    override fun stop() {
        server.close()
        serverThread.join()
    }

    override fun setOnNewConnection(callback: (Connection) -> Unit) {
        this.onNewConnection = callback
    }

}