package ru.spbau.mit.model.world

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.map.GameMap
import ru.spbau.mit.model.item.Item

interface World {
    val map: GameMap
    val creatures: MutableList<Creature>

    fun creatureAt(pos: Position): Creature?
    fun itemsAt(pos: Position): List<Item>
    fun addItemAt(pos: Position, item: Item)
    fun removeItemAt(pos: Position, item: Item)
}