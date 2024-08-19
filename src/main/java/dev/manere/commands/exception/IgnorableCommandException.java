package dev.manere.commands.exception;

import dev.manere.commands.ctx.CommandArguments;

/**
 * Exception thrown when a command execution handler is requested to be stopped.
 *
 * @see CommandArguments
 */
public class IgnorableCommandException extends RuntimeException {
}