package ru.spbau.mit.view

import ru.spbau.mit.model.Position

interface Drawable {
    fun draw(view: View, pos: Position)
}