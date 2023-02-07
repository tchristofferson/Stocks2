package com.thedasmc.stocks2.logging;

import com.thedasmc.stocks2.commands.RegisterAccountCommand;
import com.thedasmc.stocks2.commands.RegisterServerCommand;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

import java.util.Arrays;

public class Log4JFilter extends AbstractFilter {

    private static final String ISSUED_COMMAND_TEXT = "issued server command:";
    private static final String[] COMMANDS_TO_SKIP = {
        "stocks " + RegisterAccountCommand.COMMAND,
        "stocks " + RegisterAccountCommand.COMMAND_ALIAS,
        "stocks " + RegisterServerCommand.COMMAND,
        "stocks " + RegisterServerCommand.COMMAND_ALIAS
    };

    @Override
    public Result filter(LogEvent event) {
        Message candidate = null;
        if (event != null) {
            candidate = event.getMessage();
        }
        return validateMessage(candidate);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        String candidate = null;
        if (msg != null) {
            candidate = msg.toString();
        }
        return validateMessage(candidate);
    }

    private Result validateMessage(Message message) {
        if (message == null)
            return Result.NEUTRAL;

        return validateMessage(message.getFormattedMessage());
    }

    private Result validateMessage(String message) {
        if (message == null)
            return Result.NEUTRAL;

        String lowerMessage = message.toLowerCase();

        if (!lowerMessage.contains(ISSUED_COMMAND_TEXT))
            return Result.NEUTRAL;

        return Arrays.stream(COMMANDS_TO_SKIP).anyMatch(message::contains) ? Result.DENY : Result.NEUTRAL;
    }
}
