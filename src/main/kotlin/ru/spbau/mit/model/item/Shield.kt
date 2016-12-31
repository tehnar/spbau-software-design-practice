package ru.spbau.mit.model.item

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.view.View

/**
 * Modifies creature's shield stat by given value while put on
 */

class Shield(val shield: Int) : Item {
    override val usable = false
    override val wearable = true
    override val name = if (shield >= 0)"Shield of (+$shield}" else "Shield of ($shield}"

    private var isWorn = false

    override fun isUsed(): Boolean = false

    override fun isWorn(): Boolean = isWorn

    override fun use(creature: Creature): ActionResult? {
        throw UnsupportedOperationException("You cannot use $name")
    }

    override fun wear(creature: Creature): ActionResult? {
        if (isWorn) {
            throw IllegalStateException("Item is already worn")
        }
        creature.stats.shield += shield
        isWorn = true
        return ActionResult("You put on your $name")
    }

    override fun takeOff(creature: Creature): ActionResult? {
        if (!isWorn) {
            throw IllegalArgumentException("Item is not worn")
        }
        creature.stats.shield -= shield
        isWorn = false
        return ActionResult("You take off your $name")
    }

    override fun draw(view: View, pos: Position) {
        view.draw(this, pos)
    }
}