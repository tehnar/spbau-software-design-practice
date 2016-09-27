package ru.spbau.mit.softwaredesign.commands

import com.nhaarman.mockito_kotlin.mock
import org.apache.commons.io.IOUtils
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.spbau.mit.softwaredesign.Environment
import kotlin.test.assertEquals

/**
 * Created by Сева on 18.09.2016.
 */


class TestGrep {
    private val grep = GrepCommand()

    @get:Rule
    private val tmpFolder = TemporaryFolder()

    @Test
    fun testStdin() {
        val out = grep.execute(listOf("[2345]{2}"), mock<Environment>(), "123\n456 123\n475".byteInputStream())
        assertEquals(listOf("123", "456 123"), IOUtils.readLines(out, "UTF-8"))
    }

    @Test
    fun testNoArgs() {
        tmpFolder.create()
        val file1 = tmpFolder.newFile("file1")
        file1.writeText("123\n456 123")

        val file2 = tmpFolder.newFile("file2")
        file2.writeText("123 123\n321\nnot123")

        val out = grep.execute(listOf("^123", file1.path, file2.path), mock<Environment>(),
                "123\n456 123\n456".byteInputStream())
        assertEquals(listOf("123", "123 123"), IOUtils.readLines(out, "UTF-8"))
    }

    @Test
    fun testContext() {
        tmpFolder.create()
        val file1 = tmpFolder.newFile("file1")
        file1.writeText("123\n123 456\nsome test")

        val file2 = tmpFolder.newFile("file2")
        file2.writeText("123 123\n321\nnot123\nanother text")

        val out = grep.execute(listOf("-A", "1", "123$", file1.path, file2.path), mock<Environment>(),
                "123\n456 123\n456".byteInputStream())
        assertEquals(listOf("123", "123 456", "123 123", "321", "not123", "another text"), IOUtils.readLines(out, "UTF-8"))
    }

    @Test
    fun testCaseInsensitivity() {
        val out = grep.execute(listOf("-i", "pAtTeRn"), mock<Environment>(), "pattern\nPATTERN".byteInputStream())
        assertEquals(listOf("pattern", "PATTERN"), IOUtils.readLines(out, "UTF-8"))
    }

    @Test
    fun testCase() {
        val out = grep.execute(listOf("pAtTeRn"), mock<Environment>(), "pattern\nPATTERN\npAtTeRn".byteInputStream())
        assertEquals(listOf("pAtTeRn"), IOUtils.readLines(out, "UTF-8"))
    }

    @Test
    fun testWordMatching() {
        val out = grep.execute(listOf("-w", "word"), mock<Environment>(),
                "wordWord\nword 123\n123 word\n`word`\n[wordD".byteInputStream())
        assertEquals(listOf("word 123", "123 word", "`word`"), IOUtils.readLines(out, "UTF-8"))
    }
}