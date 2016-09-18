package ru.spbau.mit.softwaredesign.commands

import org.junit.Test
import ru.spbau.mit.softwaredesign.EnvironmentImpl
import kotlin.test.assertEquals

/**
 * Created by Сева on 17.09.2016.
 */

class TestEnvironment {
    @Test
    fun testSimple() {
        val env = EnvironmentImpl()
        env.setVariable("test", "123")
        assertEquals("123", env.getVariable("test"))
        env.setVariable("test", "456")
        assertEquals("456", env.getVariable("test"))
    }
}