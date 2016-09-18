package ru.spbau.mit.softwaredesign.commands

import ru.spbau.mit.softwaredesign.Environment
import java.io.InputStream
import java.nio.file.Paths

/**
 * Created by Сева on 13.09.2016.
 */

class PwdCommand: Command {
    override fun execute(args: List<String>, env: Environment, stdin: InputStream): InputStream {
        return (Paths.get(".").toAbsolutePath().toString() + "\n").byteInputStream()
    }

}