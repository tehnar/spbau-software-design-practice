package ru.spbau.mit.model.action

import ru.spbau.mit.model.world.World

interface Action {
    fun applyAction(world: World): ActionResult?
}