package ru.spbau.mit.softwaredesign

import org.apache.commons.io.IOUtils
import ru.spbau.mit.softwaredesign.commands.AssignmentCommand
import ru.spbau.mit.softwaredesign.commands.Command
import ru.spbau.mit.softwaredesign.commands.ExternalCommand
import java.io.InputStream
import java.util.*

/**
 * Created by Сева on 07.09.2016.
 */

class Shell {
    private val environment: Environment
    private val systemCommands: MutableMap<String, Command> = HashMap()
    private val parser: Parser

    constructor(environment: Environment, parser: Parser) {
        this.environment = environment
        this.parser = parser
        registerCommand(AssignmentCommand.name, AssignmentCommand())
        registerCommand(ExternalCommand.name, ExternalCommand())
    }

    fun registerCommand(name: String, command: Command) {
        systemCommands[name] = command
    }

    fun executeLine(line: String): InputStream {
        val emptyOut = "".byteInputStream()

        val commands = try {
            parser.splitLineToStatements(line, environment)
        } catch (e: IncorrectLineFormatException) {
            System.err.println("Cannot parse line: ${e.message}")
            return emptyOut
        }
        if (commands.isEmpty()) {
            return emptyOut
        }

        var stdin = System.`in`
        for (command in commands) {
            val commandToExecute = if (command.name == "=") {
                systemCommands[AssignmentCommand.name]!!
            } else if (command.name in systemCommands) {
                systemCommands[command.name]!!
            } else {
                systemCommands[ExternalCommand.name]!!
            }
            stdin = commandToExecute.execute(command.args, environment, stdin)
        }

        return stdin
    }
}