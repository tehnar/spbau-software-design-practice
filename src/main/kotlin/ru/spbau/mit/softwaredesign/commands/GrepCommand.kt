package ru.spbau.mit.softwaredesign.commands

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import org.apache.commons.io.IOUtils
import ru.spbau.mit.softwaredesign.Environment
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by Сева on 18.09.2016.
 */

/**
 * Command that searches in files for provided pattern and prints found matching lines to its output
 * It supports three options: -i (for case insensitivity), -w (for matching only whole words) and
 * -A n (for printing n extra lines after each match)
 *
 * Argument for this command should have following format: 	&#91;options] pattern &#91;file...]
 * If there are no files provided that matching is applied to command input
 */
class GrepCommand: Command {
    private val options = Options()
    private val parser = DefaultParser()

    init {
        options.addOption("i", "Case insensitivity")
        options.addOption("w", "Only whole words matching")
        options.addOption("A", true, "Print n lines after match")
    }

    override fun execute(args: List<String>, env: Environment, stdin: InputStream): InputStream {
        val commandLine = try {
            parser.parse(options, args.toTypedArray())
        } catch (e: ParseException) {
            System.err.println("Cannot parse args: ${e.message}")
            printHelp()
            return "".byteInputStream()
        }
        if (commandLine.args.isEmpty()) {
            printHelp()
            return "".byteInputStream()
        }
        val extraLines = commandLine.getOptionValue("A", "0").toInt()
        val caseInsensitive = commandLine.hasOption("i")
        val wholeMatching = commandLine.hasOption("w")

        val pattern = if (!wholeMatching) commandLine.args[0] else "(^|[^\\w])${commandLine.args[0]}($|[^\\w])"
        val regex = if (caseInsensitive) Regex(pattern, RegexOption.IGNORE_CASE) else Regex(pattern)

        var result = ""

        if (commandLine.args.size > 1) {
            for (fileName in commandLine.args.drop(1)) {
                val path = Paths.get(fileName)
                if (!Files.exists(path)) {
                    System.err.println("grep: no such file: $fileName")
                    continue
                }
                val lines = path.toFile().readLines()
                result += getMatches(lines, regex, extraLines)
            }
        } else {
            val lines = IOUtils.readLines(stdin, "UTF-8")
            result += getMatches(lines, regex, extraLines)
        }

        return result.byteInputStream()
    }

    private fun printHelp() {
        System.err.println("Usage: grep [options] pattern [file...]\nOptions:")
        for (option in options.options) {
            System.err.println(option.argName + " " + option.description)
        }
    }

    private fun getMatches(lines: List<String>, regex: Regex, extraLines: Int): String {
        var result = ""
        for ((index, line) in lines.withIndex()) {
            if (!regex.containsMatchIn(line)) {
                continue
            }
            result += lines.subList(index, (index + extraLines + 1).coerceAtMost(lines.size)).joinToString("\n") + "\n"
        }
        return result
    }

}