package ru.spbau.mit.view

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame
import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.*
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.creature.Goblin
import ru.spbau.mit.model.creature.Player
import ru.spbau.mit.model.creature.Troll
import ru.spbau.mit.model.item.PotionOfHeal
import ru.spbau.mit.model.item.RingMail
import ru.spbau.mit.model.item.Shield
import ru.spbau.mit.model.item.ShortSword
import ru.spbau.mit.model.tile.FloorTile
import ru.spbau.mit.model.tile.WallTile
import ru.spbau.mit.model.world.World
import java.awt.Color
import java.util.*

class ViewImpl: View {
    private val terminal: SwingTerminalFrame
    private val messageHistory: Queue<ActionResult> = ArrayDeque()
    private val screen: TerminalScreen

    init {
        terminal = DefaultTerminalFactory().setInitialTerminalSize(TerminalSize(80, 30)).createSwingTerminal()
        terminal.isVisible = true
        screen = TerminalScreen(terminal)
        screen.startScreen()
    }

    override fun getAction(world: World, player: Creature): Action {
        val pos = player.position
        while (true) {
            val key = terminal.readInput()
            when (key.character) {
                '2' -> return MoveAction(player, Position(pos.x, pos.y + 1))
                '4' -> return MoveAction(player, Position(pos.x - 1, pos.y))
                '5' -> return MoveAction(player, pos)
                '6' -> return MoveAction(player, Position(pos.x + 1, pos.y))
                '8' -> return MoveAction(player, Position(pos.x, pos.y - 1))
                'd' -> {
                    val itemNum = terminal.readInput()
                    if (itemNum.character in '0'..('0' + player.inventory.size) - 1) {
                        return DropItemAction(player, player.inventory[itemNum.character - '0'])
                    }
                }
                'g' -> {
                    val itemNum = terminal.readInput()
                    if (itemNum.character in '0'..('0' + world.itemsAt(pos).size) - 1) {
                        return PickItemAction(player, world.itemsAt(pos)[itemNum.character - '0'])
                    }
                }
                'w' -> {
                    val itemNum = terminal.readInput()
                    if (itemNum.character in '0'..('0' + player.inventory.size - 1)) {
                        return WearItemAction(player, player.inventory[itemNum.character - '0'])
                    }
                }
                't' -> {
                    val itemNum = terminal.readInput()
                    if (itemNum.character in '0'..('0' + player.inventory.size - 1)) {
                        return TakeOffItemAction(player, player.inventory[itemNum.character - '0'])
                    }
                }
                'u' -> {
                    val itemNum = terminal.readInput()
                    if (itemNum.character in '0'..('0' + player.inventory.size - 1)) {
                        return UseItemAction(player, player.inventory[itemNum.character - '0'])
                    }
                }
            }
        }
    }

    override fun draw(item: PotionOfHeal, pos: Position) {
        terminal.cursorPosition = TerminalPosition(pos.x, pos.y)
        terminal.setForegroundColor(TextColor.RGB(200, 0, 0))
        terminal.putCharacter('!')
    }

    override fun draw(item: ShortSword, pos: Position) {
        terminal.cursorPosition = TerminalPosition(pos.x, pos.y)
        terminal.setForegroundColor(TextColor.RGB(Color.LIGHT_GRAY.red, Color.LIGHT_GRAY.green, Color.LIGHT_GRAY.blue))
        terminal.putCharacter(')')
    }

    override fun draw(item: Shield, pos: Position) {
        terminal.cursorPosition = TerminalPosition(pos.x, pos.y)
        terminal.setForegroundColor(TextColor.RGB(0, 206, 209))
        terminal.putCharacter('[')
    }

    override fun draw(item: RingMail, pos: Position) {
        terminal.cursorPosition = TerminalPosition(pos.x, pos.y)
        terminal.setForegroundColor(TextColor.RGB(0, 0, 205))
        terminal.putCharacter('[')

    }

    override fun draw(tile: FloorTile, pos: Position) {
        terminal.cursorPosition = TerminalPosition(pos.x, pos.y)
        terminal.setForegroundColor(TextColor.RGB(Color.GRAY.red, Color.GRAY.green, Color.GRAY.blue))
        terminal.putCharacter('.')
    }

    override fun draw(tile: WallTile, pos: Position) {
        terminal.cursorPosition = TerminalPosition(pos.x, pos.y)
        terminal.setForegroundColor(TextColor.RGB(Color.GRAY.red, Color.GRAY.green, Color.GRAY.blue))
        terminal.putCharacter('#')
    }

    override fun draw(creature: Player, pos: Position) {
        terminal.cursorPosition = TerminalPosition(pos.x, pos.y)
        terminal.setForegroundColor(TextColor.RGB(Color.WHITE.red, Color.WHITE.green, Color.WHITE.blue))
        terminal.putCharacter('@')
    }

    override fun draw(creature: Goblin, pos: Position) {
        terminal.cursorPosition = TerminalPosition(pos.x, pos.y)
        terminal.setForegroundColor(TextColor.RGB(139, 69, 19))
        terminal.putCharacter('g')
    }

    override fun draw(creature: Troll, pos: Position) {
        terminal.cursorPosition = TerminalPosition(pos.x, pos.y)
        terminal.setForegroundColor(TextColor.RGB(34, 139, 34))
        terminal.putCharacter('T')
    }

    override fun drawTick(world: World, messages: List<ActionResult>) {
        terminal.clearScreen()
        for (x in 0..world.map.sizeX-1) {
            for (y in 0..world.map.sizeY-1) {
                world.map.at(x, y).draw(this, Position(x, y))
                world.itemsAt(Position(x, y)).firstOrNull()?.draw(this, Position(x, y))
            }
        }
        world.creatures.forEach { it.draw(this, it.position) }


        messages.forEach {
            if (messageHistory.size == 5) {
                messageHistory.poll()
            }
            messageHistory.add(it)
        }
        messageHistory.forEachIndexed { i, actionResult ->
            putStr(0, world.map.sizeY + 1 + i, actionResult.text)
        }

        val player = world.creatures.find { it.isPlayer }!!
        val statusLine = String.format("HP: %.1f/%.1f AC: %d SH: %d DMG: %d",
                player.stats.hp, player.stats.maxHp, player.stats.armor, player.stats.shield, player.stats.damage)

        putStr(world.map.sizeX + 3, 1, player.name)
        putStr(world.map.sizeX + 3, 2, statusLine)
        putStr(world.map.sizeX + 3, 3, "Your inventory:")
        player.inventory.forEachIndexed { i, item ->
            if (item.isWorn()) {
                putStr(world.map.sizeX + 3, 4 + i, "$i: ${item.name} (on you)")
            } else {
                putStr(world.map.sizeX + 3, 4 + i, "$i: ${item.name}")
            }
        }
        putStr(world.map.sizeX + 30, 3 , "Items on floor:")
        world.itemsAt(player.position).forEachIndexed { i, item ->
            putStr(world.map.sizeX + 30, 4 + i, "$i: ${item.name}")
        }

        terminal.setCursorVisible(false)
        terminal.flush()
    }

    override fun close() {
        screen.stopScreen()
    }

    private fun putStr(column: Int, row: Int, str: String) {
        terminal.setForegroundColor(TextColor.RGB(255, 255, 255))
        terminal.cursorPosition = TerminalPosition(column, row)
        str.forEach { terminal.putCharacter(it) }
    }
}