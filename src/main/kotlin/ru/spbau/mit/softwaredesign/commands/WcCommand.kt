package ru.spbau.mit.softwaredesign.commands

import org.apache.commons.io.IOUtils
import ru.spbau.mit.softwaredesign.Environment
import java.io.InputStream
import java.nio.file.Paths

/**
 * Created by Сева on 13.09.2016.
 */

/**
 * Command that calculates number of lines, words and bytes in given files and prints these values to its output
 * If args are not empty that each argument is interpreted as a path to a file
 * Otherwise, command's input will be taken as file input
 */
class WcCommand: Command {
    override fun execute(args: List<String>, env: Environment, stdin: InputStream): InputStream {
        var wordCount = WordCount()

        if (args.size == 0) {
            wordCount = getCount(stdin)
        } else {
            for (fileName in args) {
                val newCount = getCount(Paths.get(fileName).toFile().inputStream())
                wordCount.lineCount += newCount.lineCount
                wordCount.wordCount += newCount.wordCount
                wordCount.byteCount += newCount.byteCount
            }
        }
        return ("${wordCount.lineCount} ${wordCount.wordCount} ${wordCount.byteCount}\n").byteInputStream()
    }

    private fun getCount(input: InputStream): WordCount {
        val lines = IOUtils.readLines(input, "UTF-8")
        val joinedLines = lines.joinToString(" ")
        return WordCount(lines.size, joinedLines.split(" ").size, joinedLines.length)
    }

    private data class WordCount(var lineCount: Int = 0, var wordCount: Int = 0, var byteCount: Int = 0)

}