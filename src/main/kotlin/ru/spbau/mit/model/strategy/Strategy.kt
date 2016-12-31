package ru.spbau.mit.model.strategy

import ru.spbau.mit.model.action.Action
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.world.World

interface Strategy {
    fun getAction(world: World, me: Creature): Action
}