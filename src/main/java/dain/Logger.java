package dain;

import javax.swing.*;

public class Logger {

	private static JTextArea textArea;

	public enum LoggingLevel {
		INFO, WARN, ERROR, FATAL
	}

	public static void setTextArea(JTextArea textAreaIn) {
		textArea = textAreaIn;
	}

	public static void log(String message, LoggingLevel level) {
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
}
