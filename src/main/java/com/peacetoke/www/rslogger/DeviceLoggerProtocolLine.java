package com.peacetoke.www.rslogger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 * Describes how a line should look in the protocol
 */
public class DeviceLoggerProtocolLine implements IConvertLoggerProtocolLine
{
    public final LoggerType LoggerType = com.peacetoke.www.rslogger.LoggerType.ANALYSES;
    public final Timestamp timestamp;
    public final Class<?> thisClass;
    public final DeviceInfo deviceInfo;

    /**
     * Initialize the ProtocolLine
     * @param thisClass Describes the class where the action is performed
     * @param deviceInfo includes information about the device
     */
    public DeviceLoggerProtocolLine(Class<?> thisClass, DeviceInfo deviceInfo)
    {
        this.deviceInfo = deviceInfo;
        this.timestamp = RSLogger.getTimestamp();
        this.thisClass = thisClass;
    }

    @Override
    public LoggerType getLoggerType()
    {
        return LoggerType;
    }

    @Override
    public Timestamp getTimeStamp()
    {
        return timestamp;
    }

    @Override
    public Class<?> thisClass()
    {
        return thisClass;
    }

    @Override
    public String getMsg()
    {
        return getAverageCpuAMemoryUsage(deviceInfo);
    }

    public String getAverageCpuAMemoryUsage(DeviceInfo deviceInfo)
    {
        final String unit = "MB";
        final int precision = 1;

        return  "Ø Alloc. Mem.: " + roundDouble(deviceInfo.allocatedMemory, precision) + unit +
                ", Ø Free Mem.: " + roundDouble(deviceInfo.freeMemory,precision ) + unit +
                ", Ø Total Free Mem.: " + roundDouble(deviceInfo.totalFreeMemory, precision) + unit +
                ", Ø CPU Load: " + roundDouble(deviceInfo.cpuLoad,precision) +"%";
    }

    private static double roundDouble(double d, int places)
    {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
