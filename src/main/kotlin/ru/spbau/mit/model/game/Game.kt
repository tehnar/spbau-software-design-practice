package ru.spbau.mit.model.game

import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.world.World

interface Game {
    val world: World

    fun status(): Status
    fun tick(): List<ActionResult>

    enum class Status {
        RUNNING,
        GAME_OVER
    }
}