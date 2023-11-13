package Logging;

public class ConsoleLogger implements Logger{

    @Override
    public void Trace(String message) {
        System.out.println(message);
    }
}
