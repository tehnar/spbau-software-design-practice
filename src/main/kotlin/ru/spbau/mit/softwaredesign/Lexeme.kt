package ru.spbau.mit.softwaredesign

/**
 * Created by Сева on 14.09.2016.
 */

class Lexeme (val type: LexemeType, val string: String) {
    enum class LexemeType {
        SINGLE_QUOTED_TEXT,
        DOUBLE_QUOTED_TEXT,
        PLAIN_TEXT,
        ASSIGNMENT,
        VARIABLE,
        PIPE,
        WHITESPACE
    }

    fun getInnerString(): String {
        if (type in arrayOf(LexemeType.SINGLE_QUOTED_TEXT, LexemeType.DOUBLE_QUOTED_TEXT)) {
            return string.substring(1, string.length - 1)
        }
        if (type == LexemeType.VARIABLE) {
            return string.drop(1)
        }
        return string
    }
}