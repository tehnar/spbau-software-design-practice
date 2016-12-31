package ru.spbau.mit.model.creature

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.Action
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.strategy.Strategy
import ru.spbau.mit.model.world.World
import ru.spbau.mit.view.View

class Troll(override var position: Position,
            override val inventory: MutableList<Item>,
            private val strategy: Strategy) : Creature {


    override val name = "Troll"

    override val stats = Creature.Stats(15.0, 15.0, 1.0, 7, 3, 0)

    override val isPlayer = false

    override fun getAction(world: World): Action = strategy.getAction(world, this)

    override fun draw(view: View, pos: Position) {
        view.draw(this, pos)
    }
}