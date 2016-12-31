package ru.spbau.mit.model.tile

import ru.spbau.mit.view.Drawable

/**
 * Interface that represents single cell of map
 */
interface Tile: Drawable {
    /**
     * If creature can step on it
     */
    val passable: Boolean

    /**
     * Name of tile. Used for displaying tile's information
     */
    val name: String
}