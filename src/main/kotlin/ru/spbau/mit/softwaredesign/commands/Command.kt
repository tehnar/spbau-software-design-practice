package ru.spbau.mit.softwaredesign.commands

import ru.spbau.mit.softwaredesign.Environment
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by Сева on 07.09.2016.
 */

interface Command {
    fun execute(args: List<String>, env: Environment, stdin: InputStream): InputStream
}