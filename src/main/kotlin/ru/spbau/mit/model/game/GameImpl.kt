package ru.spbau.mit.model.game

import ru.spbau.mit.model.GameConstants
import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.world.World

class GameImpl(override val world: World): Game {
    private var status = Game.Status.RUNNING

    override fun status() = status

    override fun tick(): List<ActionResult> {
        val actions = world.creatures.map { it.getAction(world).applyAction(world) }.filterNotNull()
        val dead = world.creatures.filter { it.stats.hp <= 0 }
        world.creatures.removeAll { it.stats.hp <= 0 }
        dead.forEach {
            val pos = it.position
            it.inventory.forEach {
                if (world.itemsAt(pos).size < GameConstants.MAX_ITEM_LIST_SIZE) {
                    world.addItemAt(pos, it)
                }
            }
            if (it.isPlayer) {
                status = Game.Status.GAME_OVER
            }
        }

        world.creatures.forEach {
            it.stats.hp = Math.min(it.stats.maxHp, it.stats.hp + it.stats.regenRate)
        }

        return actions
    }
}