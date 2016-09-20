package ru.spbau.mit.softwaredesign.commands

import ru.spbau.mit.softwaredesign.Environment
import java.io.InputStream

/**
 * Created by Сева on 14.09.2016.
 */

/**
 * Assignment command. Any execute call should provide exactly two arguments in args array.
 * The value of the args[1] ]is assigned to the variable with name args[0]
 * This command ignores its input and provides an empty output
 */
class AssignmentCommand: Command {
    override fun execute(args: List<String>, env: Environment, stdin: InputStream): InputStream {
        if (args.size != 2) {
            System.err.println("Incorrect number of arguments: expected 2, found ${args.size}")
        } else {
            env.setVariable(args[0], args[1])
        }
        return "".byteInputStream()
    }

    companion object {
        val name = " assignment "
    }
}