package ru.spbau.mit.messenger

import io.grpc.ManagedChannelBuilder

class ClientGrpc: Client {
    override fun connectTo(ip: String, port: Int): Connection {
        val channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext(true).build()
        return ConnectionGrpcClient(channel)
    }

}