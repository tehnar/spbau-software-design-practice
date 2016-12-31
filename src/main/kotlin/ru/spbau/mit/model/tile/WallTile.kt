package ru.spbau.mit.model.tile

import ru.spbau.mit.model.Position
import ru.spbau.mit.view.View

/**
 * Unpassable wall
 */
class WallTile: Tile {
    override val passable = false
    override val name = "wall"

    override fun draw(view: View, pos: Position) {
        view.draw(this, pos)
    }
}