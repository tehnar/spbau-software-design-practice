package ru.spbau.mit

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.creature.Goblin
import ru.spbau.mit.model.creature.Player
import ru.spbau.mit.model.creature.Troll
import ru.spbau.mit.model.game.Game
import ru.spbau.mit.model.game.GameImpl
import ru.spbau.mit.model.item.PotionOfHeal
import ru.spbau.mit.model.item.RingMail
import ru.spbau.mit.model.item.Shield
import ru.spbau.mit.model.item.ShortSword
import ru.spbau.mit.model.map.MapLoader
import ru.spbau.mit.model.strategy.RandomWalkStrategy
import ru.spbau.mit.model.world.WorldImpl
import ru.spbau.mit.view.ViewImpl
import java.nio.file.Paths

fun main(args: Array<String>) {
    val view = ViewImpl()
    val player = Player(Position(1, 1), mutableListOf(), view)

    val creatures = mutableListOf(
            player,
            Goblin(Position(4, 2), mutableListOf(), RandomWalkStrategy()),
            Goblin(Position(4, 6), mutableListOf(ShortSword(3)), RandomWalkStrategy()),
            Troll(Position(7, 8), mutableListOf(RingMail(3)), RandomWalkStrategy()),
            Goblin(Position(12, 6), mutableListOf(), RandomWalkStrategy())
    )
    val items = listOf(
            Pair(PotionOfHeal(10), Position(13, 2)),
            Pair(Shield(3), Position(5, 5))
    )
    val world = WorldImpl(MapLoader().load(Paths.get("map/map.txt").toFile()), creatures, items)
    val game = GameImpl(world)
    view.drawTick(world, listOf())
    while (true) {
        val messages = game.tick()
        if (game.status() == Game.Status.GAME_OVER) {
            break
        }
        view.drawTick(world, messages)
    }
    view.close()
}