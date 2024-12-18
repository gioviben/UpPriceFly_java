package org.example.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class FileUtil {

    public static String getLine(String file) {
        StringBuilder line = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                line.append(currentLine).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            LogPrint.printRed("ERROR - FILE MANAGER", "The following exception was thrown during getting line from file = " + file + ": " + e.getClass().getSimpleName());
            throw new RuntimeException(e);
        }
        String out = "";
        if (!line.isEmpty()) {
            out = line.substring(0, line.length() - 1);
        }
        //LogPrint.printCyan("FILE MANAGER", "Get from " + file + " file, line = ", "\n" + out, "\n");
        return out;
    }

    public static void updateFileValue(String file, String newValue) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(newValue);
            writer.close();
        } catch (Exception e) {
            LogPrint.printRed("ERROR - FILE MANAGER", "The following exception was thrown during updating file (" + file + ") value: " + e.getClass().getSimpleName() + ", with value = " + newValue);
            throw new RuntimeException(e);
        }
        //LogPrint.printCyan("FILE MANAGER", "Updated " + file + " file with value = ", "\n" + newValue, "\n");
    }

    public static List<String> getLines(String file) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (Exception e) {
            LogPrint.printRed("ERROR - FILE MANAGER", "The following exception was thrown during getting all lines from file = " + file + ": " + e.getClass().getSimpleName());
            throw new RuntimeException(e);
        }
        //LogPrint.printCyan("FILE MANAGER", "Get from " + file + " file lines = ", "\n" + lines, "\n");
        return lines;
    }

    public static void addLine(String file, String value) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(value + "\n");
            writer.close();
        } catch (Exception e) {
            LogPrint.printRed("ERROR - FILE MANAGER", "The following exception was thrown during adding line to file = " + file + ": " + e.getClass().getSimpleName() + ", with value = " + value);
            throw new RuntimeException(e);
        }
        //LogPrint.printCyan("FILE MANAGER", "Added to " + file + " file, line = ", "\n" + value, "\n");
    }

    public static void createEmptyFile(String file) {
        try {
            Files.createFile(Paths.get(file));
        } catch (Exception e) {
            LogPrint.printRed("ERROR - FILE MANAGER", "The following exception was thrown during the creation of the empty file = " + file + ": " + e.getClass().getSimpleName());
            throw new RuntimeException(e);
        }
        //LogPrint.printUncolored("FILE MANAGER", "Created the empty file = " + file);
    }
}

