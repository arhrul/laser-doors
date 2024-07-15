package com.mygdx.game;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ErrorLogger {

    public static void logError(String message, Throwable throwable) {
        try (FileWriter fileWriter = new FileWriter("error_log.txt", true); 
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("Error Message: " + message);
            throwable.printStackTrace(printWriter);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
