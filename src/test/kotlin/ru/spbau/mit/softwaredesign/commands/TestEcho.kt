package ru.spbau.mit.softwaredesign.commands

import com.nhaarman.mockito_kotlin.mock
import org.apache.commons.io.IOUtils
import org.junit.Test
import ru.spbau.mit.softwaredesign.Environment
import kotlin.test.assertEquals

/**
 * Created by Сева on 17.09.2016.
 */

class TestEcho {
    private val echo = EchoCommand()

    @Test
    fun testSimple() {
        val out = echo.execute(listOf("123", "'456 789'"), mock<Environment>(), "".byteInputStream())
        val lines = IOUtils.readLines(out, "UTF-8")
        assertEquals(1, lines.size)
        assertEquals("123 '456 789'", lines[0])
    }
}