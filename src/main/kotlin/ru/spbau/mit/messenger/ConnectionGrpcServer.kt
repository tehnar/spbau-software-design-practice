package ru.spbau.mit.messenger

import io.grpc.stub.StreamObserver
import org.apache.logging.log4j.LogManager
import ru.spbau.mit.data.Message
import java.io.EOFException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class ConnectionGrpcServer(private val server: ServerGrpc): Connection, ChatGrpc.ChatImplBase() {
    private var LOG = LogManager.getLogger(ConnectionGrpcServer::class.java)
    private val messageQueue = LinkedBlockingQueue<Message>()
    private var observer: StreamObserver<MessageProto>? = null


    override fun processConnect(request: Void, responseObserver: StreamObserver<ConnectionResult>) {
        if (server.isStopped()) {
            responseObserver.onNext(ConnectionResult.newBuilder().setResult(false).build())
            responseObserver.onCompleted()
            return
        }
        server.stop()
        server.getOnNewConnection()(this)
        responseObserver.onNext(ConnectionResult.newBuilder().setResult(true).build())
        responseObserver.onCompleted()
    }

    override fun sendMessage(message: Message) {
        val messageProto = MessageProto.newBuilder()
                .setType(message.type.ordinal)
                .setNickName(message.userName)
                .setText(message.text)
                .build()
        observer?.onNext(messageProto) ?: throw IllegalStateException("Not connected")
    }

    override fun getMessage(): Message {
        while(isAlive() && messageQueue.isEmpty()) {
            val message = messageQueue.poll(100, TimeUnit.MILLISECONDS)
            if (message != null) {
                return message
            }
        }
        throw EOFException("Connection closed")
    }

    override fun disconnect() = server.shutdown()

    override fun isAlive(): Boolean = !server.isShutdown()

    override fun chat(responseObserver: StreamObserver<MessageProto>): StreamObserver<MessageProto> {
        LOG.info("Chat started")
        observer = responseObserver
        return object : StreamObserver<MessageProto> {
            override fun onCompleted() {
            }

            override fun onNext(message: MessageProto) {
                messageQueue.add(
                        Message(Message.MessageType.values()[message.type], message.nickName, message.text)
                )
            }

            override fun onError(t: Throwable) {
                disconnect()
                LOG.warn(t)
            }

        }
    }
}
