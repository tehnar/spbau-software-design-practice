package ru.spbau.mit.softwaredesign.commands

import ru.spbau.mit.softwaredesign.Environment
import java.io.InputStream

/**
 * Created by Сева on 13.09.2016.
 */

/**
 * Ignores its input and prints its args separated with single space and trailed by newline character to its output
 */
class EchoCommand: Command {
    override fun execute(args: List<String>, env: Environment, stdin: InputStream): InputStream {
        return (args.joinToString(" ") + "\n").byteInputStream()
    }
}