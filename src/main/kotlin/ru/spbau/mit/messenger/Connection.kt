package ru.spbau.mit.messenger

import ru.spbau.mit.data.Message

/**
 * Interface that represents connection between two users.
 */
interface Connection {
    /**
     * Sends message to other side
     */
    fun sendMessage(message: Message)

    /**
     * Reads message from other side.
     * Blocks until such message is available
     */
    fun getMessage(): Message

    /**
     * Aborts the connection
     */
    fun disconnect()

    /**
     * Returns if connection is still alive
     */
    fun isAlive(): Boolean
}