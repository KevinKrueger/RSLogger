package com.peacetoke.www.rslogger;

import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.Date;

/***********************
 * Author: Kevin Krüger
 *   Date: 07.04.2021
 ************************/

/**
 * Logger class for Development
 **/
public class RSLogger
{
    private static boolean DeveloperMode = true;
    private static Logger LOGGER;
    private static LoggerProtocol protocol;
    private static long startTime; // Start Execution Time
    private static int errorCount = 0, warningCount = 0, infoCount = 0, debugCount = 0; // Analysis
    private final static String version = "1.4.6";

    /**
     *  Initialize the RSLogger Class
     *  @param logger The Main-Instance from the log4j Logger
     *  @param DeveloperMode Describes whether the development mode releases additional features when switched on
     */
    public RSLogger(Logger logger, boolean DeveloperMode)
    {
        startTime = System.currentTimeMillis();
        setDeveloperMode(DeveloperMode);
        LOGGER = logger;
        protocol = new LoggerProtocol();
        debug(this.getClass(), "RSLogger Initialized");
    }

    /**
     * Show a info message in the Console
     * @param msg Show this Message in Console
     * @param thisClass Describes the class where the action is performed
     */
    public void log(Class<?> thisClass, String msg)
    {
        infoCount++;
        protocol.AddProtocolLine(LoggerType.INFO, getTimestamp(),thisClass, msg);
        protocol.AddDeviceProtocolLine(this.getClass(), Device.getCurrentDeviceInfo());
        if(isDeveloperMode())
            LOGGER.info(msg);
    }

    /**
     *  May only be used for try and catch or other queries where errors can be spit!
     *  Development mode is not considered!
     *  @param thisClass Describes the class where the action is performed
     *  @param msg this Message in Console
     *  @param exception Spits out information in connection with the logger about the error
     */
    public void error(Class<?> thisClass, String msg, Exception exception)
    {
        errorCount++;

        String line = String.valueOf(exception.getStackTrace()[0].getLineNumber());
        String file = exception.getStackTrace()[0].getFileName();
        String ExceptionMessage = (msg == null ? "" : msg + " ") + "Exception in File: " + file + " in Line: " + line;

        protocol.AddProtocolLine(LoggerType.ERROR, getTimestamp(),thisClass, ExceptionMessage);
        protocol.AddDeviceProtocolLine(this.getClass(), Device.getCurrentDeviceInfo());
        LOGGER.error(ExceptionMessage , exception);
    }

    /**
     *  Show a warning message in the Console
     *  @param thisClass Describes the class where the action is performed
     *  @param msg this Message in Console
     */
    public void warning(Class<?> thisClass, String msg)
    {
        warningCount++;
        protocol.AddProtocolLine(LoggerType.WARN, getTimestamp(),thisClass, msg);
        protocol.AddDeviceProtocolLine(this.getClass(),Device.getCurrentDeviceInfo());
        if(isDeveloperMode())
            LOGGER.warn(msg);
    }

    /**
     *  Displays a completed operation as a message
     *  @param thisClass Describes the class where the action is performed
     *  @param msg this Message in Console
     */
    public void done(Class<?> thisClass, String msg)
    {
        infoCount++;
        protocol.AddProtocolLine(LoggerType.DONE, getTimestamp(),thisClass, msg);
        protocol.AddDeviceProtocolLine(this.getClass(), Device.getCurrentDeviceInfo());
        if(isDeveloperMode())
            LOGGER.info(msg);
    }

    /**
     *  Displays a completed operation as a message
     *  @param thisClass Describes the class where the action is performed
     *  @param msg this Message in Console
     */
    public void debug(Class<?> thisClass, String msg)
    {
        debugCount++;
        protocol.AddProtocolLine(LoggerType.DEBUG, getTimestamp(),thisClass, msg);
        protocol.AddDeviceProtocolLine(this.getClass(), Device.getCurrentDeviceInfo());
        if(isDeveloperMode())
            LOGGER.debug(msg);
    }

    public boolean isDeveloperMode()
    {
        return DeveloperMode;
    }

    public void setDeveloperMode(boolean DeveloperMode)
    {
        RSLogger.DeveloperMode = DeveloperMode;
    }

    public String Version(){
        return version;
    }

    public static Timestamp getTimestamp()
    {
        return new Timestamp(new Date().getTime());
    }

    /**
     * Build a String that has the Logger Analysis data
     * @param thisClass Describes the class where the action is performed
     * @return Logger Analysis data(String)
     */
    public String LoggerAnalysis(Class<?> thisClass)
    {
        if(isDeveloperMode())
        {
            protocol.AddDeviceProtocolLine(this.getClass(), Device.getCurrentDeviceInfo());

            long endTime = System.currentTimeMillis();
            long elapsedTime =  endTime - startTime;
            long seconds = elapsedTime / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            return String.format("Time: %02d:%02d:%02d", hours, minutes, seconds )+ ", Errors: " + errorCount + ", Warnings: " + warningCount+", Infos: " + infoCount +", Debugs: " + debugCount;
        }
        else
            return "Activate 'DeveloperMode' to get Logger Analysis!";
    }

    /**
     * Saves the protocol in the desired location
     * @param file Describes the path where the file should be saved
     * @param thisClass Describes the class where the action is performed
     */
    public void SaveLoggerProtocol(String file,Class<?> thisClass) {
        if(isDeveloperMode())
        {
            try{
                protocol.SaveProtocol(this, protocol, file, thisClass);
            }catch (Exception exception) {
                error(this.getClass(), "Protocol couldn't saved", exception);
            }
        }else
            LOGGER.warn("Activate 'DeveloperMode' to get Logger Protocol!\";");
    }
}
