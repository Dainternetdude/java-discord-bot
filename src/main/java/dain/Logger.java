package dain;

import javax.swing.*;

public class Logger {

	private static JTextArea textArea;

	private enum LoggingLevel {
		INFO, WARN, ERROR, FATAL
	}

	public static void setTextArea(JTextArea textAreaIn) {
		textArea = textAreaIn;
	}

	private static void log(String message, LoggingLevel level) {
		String string = "[" + level;
		string = string + " @ " + java.time.LocalTime.now() + "]: ";
		string = string + message;
		string = string + "\n";

		if (Settings.useGui && textArea != null) {
			textArea.append(string);
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}

		System.out.print(string);
	}
	
	public static void info(String message) {
		log(message, LoggingLevel.INFO);
	}
	
	public static void warn(String message) {
		log(message, LoggingLevel.WARN);
	}
	
	public static void error(String message) {
		log(message, LoggingLevel.ERROR);
	}
	
	public static void fatal(String message) {
		log(message, LoggingLevel.FATAL);
	}
}
