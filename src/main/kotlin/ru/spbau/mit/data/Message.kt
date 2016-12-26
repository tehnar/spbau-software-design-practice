package ru.spbau.mit.data

data class Message(val type: MessageType, val userName: String, val text: String) {
    enum class MessageType {
        TEXT_MESSAGE,
        START_WRITING,
        STOP_WRITING
    }
}