package ru.spbau.mit.messenger

import io.grpc.ServerBuilder
import org.apache.logging.log4j.LogManager
import java.util.concurrent.TimeUnit

class ServerGrpc: Server {
    private var LOG = LogManager.getLogger(ServerGrpc::class.java);

    private lateinit var server: io.grpc.Server
    private var isShutdown = true
    private var isStopped = true
    private var onNewConnection: (Connection) -> Unit = { }

    override fun start(port: Int) {
        if (!isShutdown) {
            shutdown()
        }
        isShutdown = false
        isStopped = false
        server = ServerBuilder.forPort(port).addService(ConnectionGrpcServer(this)).build()
        server.start()
        LOG.info("gRPC server started")
    }

    override fun stop() {
        LOG.info("gRPC server stopped")
        isStopped = true
    }

    override fun setOnNewConnection(callback: (Connection) -> Unit) {
        this.onNewConnection = callback
    }

    fun getOnNewConnection() = onNewConnection

    fun shutdown() {
        server.shutdownNow()//.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
        isShutdown = true
        LOG.info("gRPC server shut down")
    }

    fun isShutdown() = isShutdown
    fun isStopped() = isStopped
}