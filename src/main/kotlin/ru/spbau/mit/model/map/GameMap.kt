package ru.spbau.mit.model.map

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.tile.Tile

interface GameMap {
    val sizeX: Int
    val sizeY: Int
    fun at(x: Int, y: Int): Tile
    fun at(pos: Position): Tile = at(pos.x, pos.y)
}