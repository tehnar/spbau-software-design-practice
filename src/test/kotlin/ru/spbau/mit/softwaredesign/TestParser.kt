package ru.spbau.mit.softwaredesign

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import kotlin.test.assertEquals

class TestParser {
    private val parser = ParserImpl()
    private val env = mock<Environment>()

    @Test
    fun testSimple() {
        assertEquals(listOf(CommandStatement("simple", listOf("tokenizer", "test"))),
                parser.splitLineToStatements("  simple   tokenizer test   ", env))
    }

    @Test
    fun testSimpleSubstitute() {
        whenever(env.getVariable("abc")).thenReturn("123")
        whenever(env.getVariable("def")).thenReturn("456")
        whenever(env.getVariable("kek")).thenReturn("")

        assertEquals(listOf(CommandStatement("some", listOf("123", "vars", "123", "456"))),
                parser.splitLineToStatements("some \$abc vars \$abc   \$def \$kek", env))
    }

    @Test
    fun testQuoteSubstitute() {
        whenever(env.getVariable("a")).thenReturn("1")
        whenever(env.getVariable("b")).thenReturn("2")
        whenever(env.getVariable("c")).thenReturn("3")
        assertEquals(listOf(CommandStatement("1", listOf(" \$b ", " 3 "))),
                parser.splitLineToStatements("\$a ' \$b ' \" \$c \" ", env))
    }

    @Test
    fun testNestedVariables() {
        val varsString = "\$a \$b \$c"
        whenever(env.getVariable("a")).thenReturn("'$varsString'")
        whenever(env.getVariable("b")).thenReturn("\"$varsString\"")
        whenever(env.getVariable("c")).thenReturn("3")
        assertEquals(listOf(CommandStatement("'$varsString'", listOf("\"$varsString\"", "3"))),
                parser.splitLineToStatements("\$a \"\$b\" \$c", env))
    }

    @Test
    fun testPipe() {
        val expected = listOf(CommandStatement("abc", listOf("def")), CommandStatement("123", listOf("456")),
                CommandStatement("789", listOf()))
        assertEquals(expected, parser.splitLineToStatements("abc def | 123 456 | 789", env))
    }
}
