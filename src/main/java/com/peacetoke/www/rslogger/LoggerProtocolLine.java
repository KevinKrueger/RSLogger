package com.peacetoke.www.rslogger;

import java.sql.Timestamp;

/**
 * Describes how a line should look in the protocol
 */
public class LoggerProtocolLine
{
    private final LoggerType LoggerType;
    private final Timestamp timestamp;
    private final Class<?> thisClass;
    private final String Msg;

    /**
     * Initialize the ProtocolLine
     * @param loggerType Describes which type of log it is
     * @param timestamp Describes the current timestamp at lease time
     * @param thisClass Describes the class where the action is performed
     * @param msg Describes the information that can be seen in the protocol
     */
    public LoggerProtocolLine(LoggerType loggerType, Timestamp timestamp, Class<?> thisClass, String msg)
    {
        this.LoggerType = loggerType;
        this.timestamp = timestamp;
        this.thisClass = thisClass;
        this.Msg = msg;
    }

    /**
     * Combines the individual snippets of information
     * @return Formatted protocol line
     */
    public String buildLine()
    {
        return "["+LoggerType.name()+"]"+
                "["+timestamp+"]"+
                "["+thisClass.getSimpleName()+"]"+
                ": " + Msg;
    }
}

