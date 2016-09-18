package ru.spbau.mit.softwaredesign

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.apache.commons.io.IOUtils
import org.junit.Test
import ru.spbau.mit.softwaredesign.commands.Command
import kotlin.test.assertEquals

/**
 * Created by Сева on 17.09.2016.
 */

class TestShell {
    private val env = mock<Environment>()
    private val parser = mock<Parser>()
    private val shell = Shell(env, parser)

    @Test
    fun testSimple() {
        val cmd = mock<Command>()
        shell.registerCommand("echo", cmd)

        val line = "echo \"123 456\" 789"
        val argsList = listOf("\"123 456\"", "789")
        whenever(parser.splitLineToStatements(line, env)).thenReturn(listOf(CommandStatement("echo", argsList)))
        whenever(cmd.execute(argsList, env, System.`in`)).thenReturn(System.`in`)

        shell.executeLine(line)
    }

    @Test
    fun testPipe() {
        val cat = mock<Command>()
        val wc = mock<Command>()

        shell.registerCommand("cat", cat)
        shell.registerCommand("wc", wc)

        val line = "cat someFile | wc"
        val catStatement = CommandStatement("cat", listOf("someFile"))
        val wcStatement = CommandStatement("wc", listOf())
        whenever(parser.splitLineToStatements(line, env)).thenReturn(listOf(catStatement, wcStatement))
        val catStream = "12345 4567".byteInputStream()
        val wcStream = "".byteInputStream()

        whenever(cat.execute(catStatement.args, env, System.`in`)).thenReturn(catStream)
        whenever(wc.execute(wcStatement.args, env, catStream)).thenReturn(wcStream)

        assertEquals(wcStream, shell.executeLine(line))
    }
}