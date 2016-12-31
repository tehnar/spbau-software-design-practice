package ru.spbau.mit.model.creature

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.Action
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.world.World
import ru.spbau.mit.view.View

class Player(override var position: Position,
             override val inventory: MutableList<Item>,
             private val view: View): Creature {
    override val name = "Tehnar"

    override val stats = Creature.Stats(100.0, 100.0, 0.1, 5, 0, 0)

    override val isPlayer = true

    override fun getAction(world: World): Action = view.getAction(world, this)

    override fun draw(view: View, pos: Position) {
        view.draw(this, pos)
    }
}