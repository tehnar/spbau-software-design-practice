package ru.spbau.mit.model.strategy

import ru.spbau.mit.model.action.Action
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.world.World

/**
 * Strategy that defines mob's actions
 */
interface Strategy {
    /**
     * Gets creature's action
     */
    fun getAction(world: World, me: Creature): Action
}