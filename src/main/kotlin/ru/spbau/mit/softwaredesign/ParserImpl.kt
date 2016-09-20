package ru.spbau.mit.softwaredesign

/**
 * Created by Сева on 07.09.2016.
 */

class ParserImpl: Parser {
    private val SINGLE_QUOTE = '\''
    private val DOUBLE_QUOTE = '"'

    override fun splitLineToStatements(line: String, environment: Environment): List<CommandStatement> {
        val lexemes = splitToLexemes(line)
        val substitutedLexemes = mutableListOf<Lexeme>()

        for (lexeme in lexemes) {
            when {
                lexeme.type == Lexeme.LexemeType.DOUBLE_QUOTED_TEXT -> {
                    val newInnerString = splitToLexemes(lexeme.getInnerString()).joinToString("",
                            transform = {
                                if (it.type == Lexeme.LexemeType.VARIABLE) {
                                    environment.getVariable(it.getInnerString())
                                } else {
                                    it.string
                                }
                            })
                    substitutedLexemes.add(Lexeme(Lexeme.LexemeType.PLAIN_TEXT, newInnerString))
                }

                lexeme.type == Lexeme.LexemeType.VARIABLE -> {
                    val str = environment.getVariable(lexeme.getInnerString())
                    for (innerLexeme in splitToLexemes(str)) {
                        substitutedLexemes.add(innerLexeme)
                    }
                }

                lexeme.type == Lexeme.LexemeType.SINGLE_QUOTED_TEXT ->
                    substitutedLexemes.add(Lexeme(Lexeme.LexemeType.PLAIN_TEXT, lexeme.getInnerString()))

                else -> substitutedLexemes.add(lexeme)
            }
        }


        return splitTokensToCommands(
                substitutedLexemes.filter { it.type != Lexeme.LexemeType.WHITESPACE }.map { it.string }
        )
    }

    private fun splitToLexemes(line: String): List<Lexeme> {
        val lexemes = mutableListOf<Lexeme>()
        var curQuote = '"'
        var curToken = ""
        var isInsideQuote = false
        var curLexemeType = Lexeme.LexemeType.PLAIN_TEXT

        for (c in line + " ") {
            if (!isInsideQuote && c in arrayOf(SINGLE_QUOTE, DOUBLE_QUOTE)) {
                curQuote = c
                curToken = "$c"
                isInsideQuote = true
                continue
            }
            if (isInsideQuote && c == curQuote) {
                curToken += c
                val type = if (c == SINGLE_QUOTE) {
                    Lexeme.LexemeType.SINGLE_QUOTED_TEXT
                } else {
                    Lexeme.LexemeType.DOUBLE_QUOTED_TEXT
                }

                lexemes.add(Lexeme(type, curToken))
                curToken = ""
                isInsideQuote = false
                continue
            }
            if (curLexemeType == Lexeme.LexemeType.VARIABLE && !c.isLetterOrDigit() && c != '_') {
                lexemes.add(Lexeme(curLexemeType, curToken))
                curToken = ""
                curLexemeType = Lexeme.LexemeType.PLAIN_TEXT
            }

            if (isInsideQuote) {
                curToken += c
                continue
            }

            if (c == '$') {
                if (curToken != "") {
                    lexemes.add(Lexeme(curLexemeType, curToken))
                }
                curLexemeType = Lexeme.LexemeType.VARIABLE
                curToken = "$c"
                continue
            }
            if (c == '|') {
                if (curToken != "") {
                    lexemes.add(Lexeme(curLexemeType, curToken))
                }
                lexemes.add(Lexeme(Lexeme.LexemeType.PIPE, "|"))
                curToken = ""
                curLexemeType = Lexeme.LexemeType.PLAIN_TEXT
                continue
            }

            if (c.isWhitespace()) {
                if (curToken != "") {
                    lexemes.add(Lexeme(curLexemeType, curToken))
                }
                lexemes.add(Lexeme(Lexeme.LexemeType.WHITESPACE, "$c"))
                curToken = ""
                curLexemeType = Lexeme.LexemeType.PLAIN_TEXT
                continue
            }
            curToken += c
        }
        return lexemes.dropLast(1)
    }

    private fun splitTokensToCommands(tokens: List<String>): List<CommandStatement> {
        val result = mutableListOf<CommandStatement>()
        if (tokens.isEmpty()) {
            return result
        }
        var commandBeginPos = 0
        for ((index, token) in (tokens + "|").withIndex()) {
            if (token == "|") {
                val commandTokens = tokens.subList(commandBeginPos, index)
                if (commandTokens.size == 0) {
                    throw IncorrectLineFormatException("Cannot parse expression before $token)")
                }
                if (commandTokens.size == 3 && commandTokens[1] == "=") {
                    result.add(CommandStatement("=", listOf(commandTokens[0], commandTokens[2])))
                } else {
                    result.add(CommandStatement(commandTokens[0], commandTokens.drop(1)))
                }
                commandBeginPos = index + 1
            }
        }
        return result
    }
}