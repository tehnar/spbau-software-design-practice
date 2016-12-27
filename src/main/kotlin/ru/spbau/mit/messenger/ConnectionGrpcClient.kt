package ru.spbau.mit.messenger

import io.grpc.ManagedChannel
import io.grpc.stub.StreamObserver
import org.apache.logging.log4j.LogManager
import ru.spbau.mit.data.Message
import java.io.EOFException
import java.io.IOException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class ConnectionGrpcClient(private val channel: ManagedChannel) : Connection, ChatGrpc.ChatImplBase() {
    private var LOG = LogManager.getLogger(ConnectionGrpcClient::class.java)

    private var chatStub: ChatGrpc.ChatStub
    private val messageQueue = LinkedBlockingQueue<Message>()
    private var observer: StreamObserver<MessageProto>? = null

    init {
        chatStub = ChatGrpc.newStub(channel)
        chatStub.processConnect(Void.getDefaultInstance(), object: StreamObserver<ConnectionResult> {
            var result = true

            override fun onNext(result: ConnectionResult) {
                this.result = result.result
            }

            override fun onCompleted() {
                if (result) {
                    startChat()
                } else {
                    throw IOException("Server rejected connection")
                }
            }

            override fun onError(t: Throwable) {
                LOG.warn(t)
            }

        })
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

    override fun disconnect() {
        channel.shutdownNow()
    }

    override fun isAlive(): Boolean = !channel.isTerminated

    private fun startChat() {
        LOG.info("Chat started")
        observer = chatStub.chat(object : StreamObserver<MessageProto> {
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
        })
    }
}
