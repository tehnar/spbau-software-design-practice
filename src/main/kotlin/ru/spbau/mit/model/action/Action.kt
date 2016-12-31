package ru.spbau.mit.model.action

import ru.spbau.mit.model.world.World

/**
 * Interface that represents creature's action
 */
interface Action {
    /**
     * Applies action to world
     * @return description of action performed
     */
    fun applyAction(world: World): ActionResult?
}