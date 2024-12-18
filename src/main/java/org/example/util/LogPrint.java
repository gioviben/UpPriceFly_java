package org.example.util;

import org.fusesource.jansi.Ansi;

public class LogPrint {

    public static void printGreen(String messageType, String text) {
        System.out.print(messageType + " --> ");
        System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a(text).reset());
    }

    public static void printRed(String messageType, String text) {
        System.out.print(messageType + " --> ");
        System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a(text).reset());
    }

    public static void printYellow(String messageType, String text) {
        System.out.print(messageType + " --> ");
        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a(text).reset());
    }

    public static void printUncolored(String messageType, String text) {
        System.out.println(messageType + " --> " + text);
    }

    public static void printCyan(String messageType, String text, String textColored, String end) {
        System.out.print(messageType + " --> ");
        if (textColored == null) {
            System.out.print(Ansi.ansi().fg(Ansi.Color.CYAN).a(text).reset());
        } else {
            System.out.print(text);
            System.out.print(Ansi.ansi().fg(Ansi.Color.CYAN).a(textColored).reset());
        }
        System.out.print(end);
    }

    public static void printMagenta(String messageType, String text) {
        System.out.print(messageType + " --> ");
        System.out.println(Ansi.ansi().fg(Ansi.Color.MAGENTA).a(text).reset());
    }

    public static void printBlue(String messageType, String text) {
        System.out.print(messageType + " --> ");
        System.out.println(Ansi.ansi().fg(Ansi.Color.BLUE).a(text).reset());
    }

    public static void printTitle(String title) {
        System.out.println(Ansi.ansi().fg(Ansi.Color.MAGENTA).a(title).reset());
    }
}

