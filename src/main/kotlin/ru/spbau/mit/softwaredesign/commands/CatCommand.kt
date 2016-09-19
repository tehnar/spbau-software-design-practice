package ru.spbau.mit.softwaredesign.commands

import ru.spbau.mit.softwaredesign.Environment
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by Сева on 13.09.2016.
 */

/**
 * Command that prints content of files to output
 * If args are not empty that each argument is interpreted as a path to a file
 * Otherwise this command returns its input as output
 */
class CatCommand: Command {
    override fun execute(args: List<String>, env: Environment, stdin: InputStream): InputStream {
        if (args.size == 0) {
            return stdin
        }
        var content = ""
        for (fileName in args) {
            val path = Paths.get(fileName)
            if (!Files.exists(path)) {
                System.err.println("cat: no such file: $fileName")
                continue
            }
            content += path.toFile().readLines().joinToString("\n")
        }
        return (content + "\n").byteInputStream()
    }

}