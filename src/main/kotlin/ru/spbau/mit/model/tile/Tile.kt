package ru.spbau.mit.model.tile

import ru.spbau.mit.view.Drawable

interface Tile: Drawable {
    val passable: Boolean
    val name: String
}