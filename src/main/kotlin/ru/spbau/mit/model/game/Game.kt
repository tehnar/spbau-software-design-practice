package ru.spbau.mit.model.game

import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.world.World

/**
 * Interface that represents current game
 */
interface Game {
    /**
     * Game's world
     */
    val world: World

    /**
     * Status of game
     */
    fun status(): Status

    /**
     * Asks every creature for action and applies them to world
     */
    fun tick(): List<ActionResult>

    enum class Status {
        RUNNING,
        GAME_OVER
    }
}