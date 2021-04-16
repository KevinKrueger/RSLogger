# RSLogger 

This Logger is an Extended Version of Log4j. <br>
It use the Library of apache.log4j core and api.

## Benefits:
    -   You can only see logs when DeveloperMode is enabled
    -   Get a Protocol(File) from the collected log data
    -   Get the Logger Analysis data
    -   Display the average load
    -   Completely customizable for console output

## Documentation (Current version)
<a href="https://documentation.peacetoke.com/rslogger/1_4_6">RSLogger - Documentation(Link)</a>

## Example:
```java
public class Main
{
    // Init: DevelperMode = true
    final static RSLogger logger = new RSLogger(LogManager.getLogger(), true);

    public static void main(String[] args)
    {
        logger.log(Main.class, "Hello World");
    }
        
}
```
## For Your Extensions:
If you want to make the logger available everywhere,
you can create an interface which passes the instance.<br>
This way you can collect information in every class!

```java
public interface ILogger
{
    static RSLogger LOGGER = Main.logger;
}
```


## Custom Console Output
Create a file named: 'log4j2.xml' in the 'resource' folder(Recommended).<br>

File content:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
    <Appenders>
        <Console name="CONSOLE" target="SYSTEM_OUT">
            <PatternLayout pattern="%highlight{[%-level][%d{yyyy-MM-dd HH:mm:ss.SSS}][%t] %c{1} - %msg%n}{FATAL=red blink, ERROR=red, WARN=yellow bold, INFO=green, DEBUG=gray, TRACE=blue}" disableAnsi="false"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="ALL">
            <AppenderRef ref="CONSOLE"/>
        </Root>
    </Loggers>
</Configuration>
```