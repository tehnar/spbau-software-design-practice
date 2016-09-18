package ru.spbau.mit.softwaredesign

/**
 * Created by Сева on 07.09.2016.
 */

class EnvironmentImpl: Environment {
    private val variables: MutableMap<String, String> = hashMapOf()

    init {
        System.getenv().forEach { variables[it.key] = it.value }
    }

    override fun setVariable(name: String, value: String) {
        variables[name] = value
    }

    override fun getVariable(name: String) = variables[name].orEmpty()
}