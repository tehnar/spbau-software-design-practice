package ru.spbau.mit.model.item

import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.view.Drawable

interface Item: Drawable {
    val usable: Boolean
    val wearable: Boolean
    val name: String

    fun isUsed(): Boolean
    fun isWorn(): Boolean
    fun use(creature: Creature): ActionResult?
    fun wear(creature: Creature): ActionResult?
    fun takeOff(creature: Creature): ActionResult?
}