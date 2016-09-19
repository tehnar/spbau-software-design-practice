package ru.spbau.mit.softwaredesign

/**
 * Created by Сева on 14.09.2016.
 */

/**
 * Interface for storing and retrieving environment variables
 */
interface Environment {
    fun setVariable(name: String, value: String)
    fun getVariable(name: String): String
}