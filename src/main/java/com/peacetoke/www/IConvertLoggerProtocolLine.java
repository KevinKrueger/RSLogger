package com.peacetoke.www;

import java.sql.Timestamp;

/**
 * Convert your Own Information-Protocol-Line into the Standard LoggerProtocolLine
 */
public interface IConvertLoggerProtocolLine
{
    RSLogger.LoggerType getLoggerType();
    Timestamp getTimeStamp();
    Class<?> thisClass();
    String getMsg();
}
