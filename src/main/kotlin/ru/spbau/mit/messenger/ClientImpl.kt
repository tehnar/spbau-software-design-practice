package ru.spbau.mit.messenger

import java.net.Socket

class ClientImpl: Client {
    override fun connectTo(ip: String, port: Int): Connection {
        return ConnectionImpl(Socket(ip, port))
    }

}