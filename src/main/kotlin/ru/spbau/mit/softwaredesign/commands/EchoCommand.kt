package ru.spbau.mit.softwaredesign.commands

import ru.spbau.mit.softwaredesign.Environment
import java.io.InputStream

/**
 * Created by Сева on 13.09.2016.
 */

class EchoCommand: Command {
    override fun execute(args: List<String>, env: Environment, stdin: InputStream): InputStream {
        return (args.joinToString(" ") + "\n").byteInputStream()
    }

}