package com.peacetoke.www.rslogger;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * With the LoggerProtocol, you can create an Protocol from the Logger
 */
public class LoggerProtocol
{
    private static List<LoggerProtocolLine> loggerProtocolLineList;
    private final List<DeviceLoggerProtocolLine> deviceLoggerProtocolLineList;

    public LoggerProtocol()
    {
        loggerProtocolLineList = new ArrayList<>();
        deviceLoggerProtocolLineList = new ArrayList<>();
    }

    /**
     * Adds a line to the protocol
     * @param loggerType Describes which type of log it is
     * @param timestamp Describes the current timestamp at lease time
     * @param thisClass Describes the class where the action is performed
     * @param msg Describes the information that can be seen in the protocol
     */
    public void AddProtocolLine(LoggerType loggerType, Timestamp timestamp, Class<?> thisClass, String msg)
    {
        loggerProtocolLineList.add(
                new LoggerProtocolLine(
                        loggerType,
                        timestamp,
                        thisClass,
                        msg
                )
        );
    }

    /**
     * Adds the actual CPU and Memory usage to the List
     * @param thisClass Describes the class where the action is performed
     * @param deviceInfo includes information about the device
     */
    public void AddDeviceProtocolLine(Class<?> thisClass, DeviceInfo deviceInfo)
    {
        deviceLoggerProtocolLineList.add(new DeviceLoggerProtocolLine(thisClass, deviceInfo));
    }

    /**
     * Converts your own ProtocolLine
     * @param convertLPL To Converted Line
     * @return A Converted LoggerProtocolLine
     */
    public LoggerProtocolLine ConvertToLoggerProtocolLine(IConvertLoggerProtocolLine convertLPL)
    {
        return new LoggerProtocolLine(convertLPL.getLoggerType(), convertLPL.getTimeStamp(), convertLPL.thisClass(), convertLPL.getMsg());
    }

    /**
     * Saves the protocol in the desired location
     * @param protocol The instance of the protocol
     * @param file Describes the path where the file should be saved
     * @param thisClass Describes the class where the action is performed
     */
    public void SaveProtocol(LoggerProtocol protocol, String file, Class<?> thisClass) throws IOException {

        PrintWriter printWriter = new PrintWriter(new FileWriter(file));

        // Start Line
        printWriter.print("======= RSLogger Protocol from "+ RSLogger.getTimestamp().toLocalDateTime().toLocalDate()+" ======="+"\n");

        // Change Analyze Prosition in file under RSLogger
        MoveObjectToListIndex(loggerProtocolLineList, 0); // LoggerAnalysis
        CalculateAveragedMemoryACPU(protocol, thisClass, deviceLoggerProtocolLineList);
        MoveObjectToListIndex(loggerProtocolLineList, 1); // DeviceAnalysis

        // Write in File
        for (LoggerProtocolLine protocolLine : loggerProtocolLineList)
        {
            printWriter.print(protocolLine.buildLine()+"\n");
        }

        printWriter.close();
    }

    /**
     *  Calculates the Average of the Memory and CPU
     * @param protocol The instance of the protocol
     * @param thisClass Describes the class where the action is performed
     * @param deviceLoggerProtocolLineList The instance of the DeviceLogger-ProtocolLine-List
     */
    private static void CalculateAveragedMemoryACPU(LoggerProtocol protocol, Class<?> thisClass, List<DeviceLoggerProtocolLine> deviceLoggerProtocolLineList)
    {
        int listSize = deviceLoggerProtocolLineList.size();
        DeviceInfo average_memory_device = new DeviceInfo();

        for(DeviceLoggerProtocolLine deviceLine : deviceLoggerProtocolLineList)
        {
            average_memory_device.cpuLoad += deviceLine.deviceInfo.cpuLoad;
            average_memory_device.allocatedMemory += deviceLine.deviceInfo.allocatedMemory;
            average_memory_device.freeMemory += deviceLine.deviceInfo.freeMemory;
            average_memory_device.totalFreeMemory += deviceLine.deviceInfo.totalFreeMemory;
        }

        // Calculate Average
        average_memory_device.cpuLoad = average_memory_device.cpuLoad / listSize;
        average_memory_device.allocatedMemory = average_memory_device.allocatedMemory / listSize;
        average_memory_device.freeMemory = average_memory_device.freeMemory / listSize;
        average_memory_device.freeMemory = average_memory_device.freeMemory / listSize;

        DeviceLoggerProtocolLine deviceLine = new DeviceLoggerProtocolLine(thisClass, average_memory_device);
        loggerProtocolLineList.add(protocol.ConvertToLoggerProtocolLine(deviceLine));
    }

    /**
     * Can only move the last object in a list
     * @param list The instance of the list
     * @param index The index of the new Position
     */
    private static void MoveObjectToListIndex(List<LoggerProtocolLine> list, int index)
    {
        LoggerProtocolLine item = list.get(list.size() - 1);
        list.remove(item);
        list.add(index, item);
    }

    /**
     * Can move objects in a list
     * @param list The instance of the list
     * @param currentIndex The Current position of an object your want to move in the list
     * @param newIndex The Position of the new object
     */
    private static void MoveObjectToListIndex(List<LoggerProtocolLine> list, int currentIndex, int newIndex)
    {
        LoggerProtocolLine item = list.get(currentIndex);
        list.remove(item);
        list.add(newIndex, item);
    }
}