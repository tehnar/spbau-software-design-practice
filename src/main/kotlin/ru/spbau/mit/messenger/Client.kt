package ru.spbau.mit.messenger

interface Client {
    /**
     * Connects to given address
     */
    fun connectTo(ip: String, port: Int): Connection
}