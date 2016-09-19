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

/**
 * Provides an interface for execution of shell commands
 * All the commands except for AssignmentCommand and ExternalCommand should be registered to be recognized
 * Any unregistered command is interpreted as external
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

    /**
     * Maps provided name to a particular command.
     */
    fun registerCommand(name: String, command: Command) {
        systemCommands[name] = command
    }

    /**
     * Interprets line as a command (or as a pipe of commands), executes them sequentially
     * and returns output of the last command
     * All the commands used in line should be registered before execution.
     * Any unregistered command is interpreted as external
     *
     * @param line Line to execute
     * @return InputStream of output of the last command in pipe
     */
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
            var args = command.args
            val commandToExecute = if (command.name == "=") {
                systemCommands[AssignmentCommand.name]!!
            } else if (command.name in systemCommands) {
                systemCommands[command.name]!!
            } else {
                args = listOf(command.name) + args
                systemCommands[ExternalCommand.name]!!
            }
            stdin = commandToExecute.execute(args, environment, stdin)
        }

        return stdin
    }
}