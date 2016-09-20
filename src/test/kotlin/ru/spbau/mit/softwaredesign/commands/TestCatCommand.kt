package ru.spbau.mit.softwaredesign.commands

import com.nhaarman.mockito_kotlin.mock
import org.apache.commons.io.IOUtils
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.spbau.mit.softwaredesign.Environment
import kotlin.test.assertEquals

/**
 * Created by Сева on 17.09.2016.
 */

class TestCatCommand {
    private val cat = CatCommand()

    @get:Rule
    private val tmpFolder = TemporaryFolder()

    @Test
    fun testStdin() {
        val testString = "abc def"
        val out = cat.execute(listOf(), mock<Environment>(), testString.byteInputStream())
        val lines = IOUtils.readLines(out, "UTF-8")
        assertEquals(1, lines.size)
        assertEquals(testString, lines[0])
    }

    @Test
    fun testFiles(){
        tmpFolder.create()
        val testFile1 = tmpFolder.newFile("testFile1")
        val testFile2 = tmpFolder.newFile("testFile2")
        val testStr1 = "aba\ncaba"
        val testStr2 = "1 2 3"
        testFile1.outputStream().write(testStr1.toByteArray())
        testFile2.outputStream().write(testStr2.toByteArray())

        val out = cat.execute(listOf(testFile1.path, testFile2.path), mock<Environment>(),
                "123".byteInputStream())
        val lines = IOUtils.readLines(out, "UTF-8")
        assertEquals("$testStr1$testStr2", lines.joinToString("\n"))
    }
}