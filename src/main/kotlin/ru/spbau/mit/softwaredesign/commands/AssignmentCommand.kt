package ru.spbau.mit.softwaredesign.commands

import org.apache.commons.io.IOUtils
import ru.spbau.mit.softwaredesign.commands.Command
import ru.spbau.mit.softwaredesign.Environment
import java.io.InputStream

/**
 * Created by Сева on 14.09.2016.
 */

class AssignmentCommand: Command {
    override fun execute(args: List<String>, env: Environment, stdin: InputStream): InputStream {
        env.setVariable(args[0], args[1])
        return IOUtils.toInputStream("", "UTF-8")
    }

    companion object {
        val name = " assignment "
    }
}