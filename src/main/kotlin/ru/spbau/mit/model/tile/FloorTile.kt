package ru.spbau.mit.model.tile

import ru.spbau.mit.model.Position
import ru.spbau.mit.view.View

class FloorTile: Tile {
    override val passable = true
    override val name = "floor"

    override fun draw(view: View, pos: Position) {
        view.draw(this, pos)
    }
}