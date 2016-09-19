package ru.spbau.mit.softwaredesign

/**
 * Created by Сева on 17.09.2016.
 */

interface Parser {
    /**
     * Substitutes variables in given line from environment, tokenizes it and
     * returns result of splitting by 'pipe' token
     * @param line Line to split
     * @param environment environment variables to substitute
     */
    fun splitLineToStatements(line: String, environment: Environment): List<CommandStatement>
}