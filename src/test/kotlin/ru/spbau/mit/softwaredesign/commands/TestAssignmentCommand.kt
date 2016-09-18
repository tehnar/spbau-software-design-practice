package ru.spbau.mit.softwaredesign.commands

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import org.junit.Test
import ru.spbau.mit.softwaredesign.Environment

/**
 * Created by Сева on 17.09.2016.
 */

class TestAssignmentCommand {
    private val assignment = AssignmentCommand()

    @Test
    fun test() {
        val env = mock<Environment>()
        assignment.execute(listOf("a", "123"), env, "".byteInputStream())
        verify(env, times(1)).setVariable("a", "123")
        verifyNoMoreInteractions(env)
    }
}