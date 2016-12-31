package ru.spbau.mit.model.map

import ru.spbau.mit.model.tile.FloorTile
import ru.spbau.mit.model.tile.Tile
import ru.spbau.mit.model.tile.WallTile
import java.io.File

/**
 * Utility class that loads pregenerated map from disk
 */
class MapLoader {
    fun load(file: File): GameMap {
        val lines = file.readLines()
        val (sizeX, sizeY) = lines[0].split(" ").map(String::toInt)
        val map = Array(sizeX, { Array<Tile>(sizeY, { FloorTile() }) })
        for (i in 0..sizeY-1) {
            for (j in 0..sizeX-1) {
                map[j][i] = when (lines[i + 1][j]) {
                    '#' -> WallTile()
                    '.' -> FloorTile()
                    else -> throw IllegalStateException("Unknown tile ${lines[i + 1][j]}")
                }
            }
        }
        return GameMapImpl(sizeX, sizeY, map)
    }
}