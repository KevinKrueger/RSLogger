package com.peacetoke.www.rslogger;

import java.sql.Timestamp;

/**
 * Convert your Own Information-Protocol-Line into the Standard LoggerProtocolLine
 */
public interface IConvertLoggerProtocolLine
{
    LoggerType getLoggerType();
    Timestamp getTimeStamp();
    Class<?> thisClass();
    String getMsg();
}
