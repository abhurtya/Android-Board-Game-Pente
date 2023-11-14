package edu.ramapo.abhurtya.pente.utils;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static Logger instance; // Singleton instance
    private List<String> logList;

    // Private constructor
    private Logger() {
        logList = new ArrayList<>();
    }

    // Public method to get the instance
//    only one thread can execute that method at a time for an instance of the class.
//    https://www.baeldung.com/java-synchronized
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    // Adds a log entry
    public void addLog(String log) {
        logList.add(log);
    }

    // Clears all log entries
    public void clearLogs() {
        logList.clear();
    }

    // Returns all logs as a String
    public String showLogs() {
        StringBuilder logs = new StringBuilder();
        for (int i = logList.size() - 1; i >= 0; i--) {
            logs.append(logList.get(i)).append("\n");
        }
        return logs.toString();
    }


    // Returns the last log entry, or null if no logs are present
    public String showLastLog() {
        if (!logList.isEmpty()) {
            return logList.get(logList.size() - 1);
        }
        return null;
    }

    // This will write all logs to a file
    // public void writeLogsToFile(Context context) {
    //     try (FileOutputStream fos = context.openFileOutput("logs.txt", Context.MODE_PRIVATE)) {
    //         fos.write(showLogs().getBytes());
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}
