package com.peacetoke.www;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main
{
    final static Logger LOGGER = LogManager.getLogger(Main.class);
    final static RSLogger logger = new RSLogger(LOGGER, true);

    public static void main(String[] args) throws Exception {
        LOGGER.error("Hello World");
        logger.SaveLoggerProtocol("B:\\Protocol.ptc", Main.class);
    }
}
