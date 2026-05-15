package cz.vsb.fei.java2;

import cz.vsb.fei.java2.lab01text2asciiart.ConversionException;
import cz.vsb.fei.java2.lab01text2asciiart.Text2AsciiArt;

import java.util.Scanner;

public class App {

	public static void main(String[] args) {
		try {
			String text = loadText(args);
			String asciiArt = new Text2AsciiArt().convert(text);
			System.out.println(asciiArt);
		} catch (IllegalArgumentException | ConversionException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	private static String loadText(String[] args) {
		String textFromParameter = getTextParameter(args);
		if (textFromParameter != null) {
			return textFromParameter;
		}

		return readTextFromConsole();
	}

	private static String getTextParameter(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if ("-text".equals(args[i])) {
				if (i + 1 >= args.length) {
					throw new IllegalArgumentException("Parameter -text musi obsahovat text.");
				}
				return args[i + 1];
			}
		}
		return null;
	}

	private static String readTextFromConsole() {
		Scanner scanner = new Scanner(System.in);
		if (!scanner.hasNextLine()) {
			return "";
		}
		return scanner.nextLine();
	}
}
