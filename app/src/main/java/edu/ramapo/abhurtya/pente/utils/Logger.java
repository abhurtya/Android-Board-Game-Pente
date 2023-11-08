package edu.ramapo.abhurtya.pente.utils;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private List<String> logList;

    public Logger() {
        logList = new ArrayList<>();
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
        for (String log : logList) {
            logs.append(log).append("\n");
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
//    public void writeLogsToFile(Context context) {
//        try (FileOutputStream fos = context.openFileOutput("logs.txt", Context.MODE_PRIVATE)) {
//            fos.write(showLogs().getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}


