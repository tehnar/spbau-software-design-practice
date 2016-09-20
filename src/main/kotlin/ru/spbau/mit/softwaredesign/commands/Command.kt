package ru.spbau.mit.softwaredesign.commands

import ru.spbau.mit.softwaredesign.Environment
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by Сева on 07.09.2016.
 */

/**
 * Interface for commands
 * Classes that implement this interface should not have any inner state that is stored between
 * sequential calls of execute method
 * Also these commands should not throw exceptions.
 * Any error should be handled inside execute method and error message should be printed to System.err
 */
interface Command {
    /**
     * Executes command with given arguments, environment and input.
     * Returns output of the command as InputStream
     *
     * @param args list of command arguments
     * @param env environment. Could be changed during execution
     * @param stdin input of the command
     *
     * @return output of the command
     */
    fun execute(args: List<String>, env: Environment, stdin: InputStream): InputStream
}