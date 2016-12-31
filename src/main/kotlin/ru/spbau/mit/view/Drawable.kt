package ru.spbau.mit.view

import ru.spbau.mit.model.Position

/**
 * Represents that object can be drawn at view
 */
interface Drawable {
    fun draw(view: View, pos: Position)
}