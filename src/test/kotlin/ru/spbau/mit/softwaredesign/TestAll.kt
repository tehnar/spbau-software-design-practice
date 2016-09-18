package ru.spbau.mit.softwaredesign

import org.apache.commons.io.IOUtils
import org.junit.Test
import ru.spbau.mit.softwaredesign.commands.AssignmentCommand
import ru.spbau.mit.softwaredesign.commands.EchoCommand
import ru.spbau.mit.softwaredesign.commands.WcCommand
import kotlin.test.assertEquals

/**
 * Created by Сева on 17.09.2016.
 */

class TestAll {
    private val parser = ParserImpl()
    private val env = EnvironmentImpl()
    private val shell = Shell(env, parser)

    @Test
    fun testAll() {
        shell.registerCommand("echo", EchoCommand())
        shell.registerCommand("wc", WcCommand())

        shell.executeLine("a = 123")
        shell.executeLine("b = 456")
        shell.executeLine("c = \$a")
        val lines = IOUtils.readLines(shell.executeLine("echo \$a \$b \$c | wc"), "UTF-8")

        assertEquals(1, lines.size)
        assertEquals("1 3 11", lines[0])
    }
}