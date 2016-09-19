package ru.spbau.mit.softwaredesign.commands

import org.apache.commons.io.IOUtils
import ru.spbau.mit.softwaredesign.Environment
import java.io.IOException
import java.io.InputStream

/**
 * Created by Сева on 07.09.2016.
 */

/**
 * Executes external command
 * Its name is the first argument and other arguments are passed to an external command
 * Command takes stdin as its input
 * Returns output of executed command
 */
class ExternalCommand: Command {
    override fun execute(args: List<String>, env: Environment, stdin: InputStream): InputStream {
        val processBuilder = ProcessBuilder(args)
        if (stdin == System.`in`) {
            processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)
        } else {
            processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE)
        }

        val process = try {
            processBuilder.start()
        } catch (e: IOException) {
            System.err.println("Cannot run ${args[0]}: " + e.cause)
            return "".byteInputStream()
        }

        if (stdin != System.`in`) {
            IOUtils.copy(stdin, process.outputStream)
            process.outputStream.close()
        }
        process.waitFor()
        return process.inputStream
    }

    companion object {
        val name = "externalCommand"
    }
}