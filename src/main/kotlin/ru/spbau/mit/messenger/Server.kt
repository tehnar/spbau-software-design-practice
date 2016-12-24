package ru.spbau.mit.messenger

interface Server {
    /**
     * Starts server on specified port.
     * Consecutive calls of this method should be separated by stop method call
     */
    fun start(port: Int)

    /**
     * Stops the server.
     */
    fun stop()

    /**
     * Callback which is called when some user connected to the server
     * Only one user can connect during the single server lifecycle
     */
    fun setOnNewConnection(callback: (Connection) -> Unit)
}