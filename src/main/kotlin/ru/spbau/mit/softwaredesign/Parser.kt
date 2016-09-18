package ru.spbau.mit.softwaredesign

/**
 * Created by Сева on 17.09.2016.
 */

interface Parser {
    fun splitLineToStatements(line: String, environment: Environment): List<CommandStatement>
}