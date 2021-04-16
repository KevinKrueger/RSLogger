package com.peacetoke.www.rslogger;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Gets your device info
 */
public class Device
{

    /**
     * Gets the CPU usage of the application
     * @return Double: CPU Load
     */
    public static double getProcessCpuLoad()
    {
        MBeanServer mbs = null;
        ObjectName name = null;
        AttributeList list = new AttributeList();

        try
        {
            mbs    = ManagementFactory.getPlatformMBeanServer();
            name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
            list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        if (list.isEmpty())     return Double.NaN;

        Attribute att = (Attribute)list.get(0);
        Double value  = (Double)att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0)      return Double.NaN;
        // returns a percentage value with 1 decimal point precision
        return ((int)(value * 1000) / 10.0);
    }

    public static DeviceInfo getCurrentDeviceInfo()
    {
        Runtime runtime = Runtime.getRuntime();
        DeviceInfo deviceMemory= new DeviceInfo();
        final double toMB = Math.pow(10, -6);
        deviceMemory.maxMemory = runtime.maxMemory() * toMB; // MB
        deviceMemory.allocatedMemory = runtime.totalMemory() * toMB;// MB
        deviceMemory.freeMemory = runtime.freeMemory() * toMB;// MB
        deviceMemory.totalFreeMemory = (deviceMemory.freeMemory + (deviceMemory.maxMemory - deviceMemory.allocatedMemory));

        deviceMemory.cpuLoad = getProcessCpuLoad();

        return deviceMemory;
    }
}
