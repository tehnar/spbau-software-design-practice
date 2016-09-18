package ru.spbau.mit.softwaredesign

import org.apache.commons.io.IOUtils
import ru.spbau.mit.softwaredesign.commands.*

/**
 * Created by Сева on 13.09.2016.
 */


fun main(args: Array<String>) {
    val shell = Shell(EnvironmentImpl(), ParserImpl())
    shell.registerCommand("echo", EchoCommand())
    shell.registerCommand("pwd", PwdCommand())
    shell.registerCommand("wc", WcCommand())
    shell.registerCommand("cat", CatCommand())

    while (true) {
        print("$")
        val line = readLine()!!
        if (line == "exit") {
            return
        }
        IOUtils.copy(shell.executeLine(line), System.out)
    }
}