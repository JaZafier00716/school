package cz.vsb.fei.java2;

import cz.vsb.fei.java2.lab01text2asciiart.ConversionException;
import cz.vsb.fei.java2.lab01text2asciiart.Text2AsciiArt;
import lombok.extern.log4j.Log4j2;

import java.io.Console;
import java.util.Objects;
import java.util.Scanner;

/**
 *  Class <b>App</b> - main class
 */
@Log4j2
public class App {
    static String text;
	public static void main(String[] args) {
        Text2AsciiArt text2AsciiArt = new Text2AsciiArt();

        if(args.length < 1) {
            log.error("Invalid number of arguments. Expected 2, got " + args.length);
            log.info("Usage: java -jar app.jar -text myTextForConversion");
            log.info("Usage: java -jar app.jar -cli");
            return;
        }

        if(args.length == 1) {
            if(!args[0].equals("-cli")) {
                log.error("Invalid argument. Expected '-cli' or '-text'");
                log.info("Usage: java -jar app.jar -text myTextForConversion");
                log.info("Usage: java -jar app.jar -cli");
                return;
            } else {
                // Interactive CLI mode: read text from console
                Console console = System.console();
                if (console != null) {
                    String input = console.readLine("Enter text to convert: ");
                    while (input == null || input.isEmpty()) {
                        log.error("Input text cannot be empty.");
                        input = console.readLine("Enter text to convert: ");
                    }
                    text = input;
                } else {
                    // Fall back to Scanner when Console is not available (IDEs / redirected IO)
                    log.info("Console not available, falling back to Scanner(System.in). Please type the text and press Enter.");
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Enter text to convert: ");
                    String input = scanner.nextLine();
                    while (input == null || input.isEmpty()) {
                        log.error("Input text cannot be empty.");
                        System.out.print("Enter text to convert: ");
                        input = scanner.nextLine();
                    }
                    text = input;
                    // don't close the scanner because it would close System.in
                }
            }
        } else {
            if(args.length != 2) {
                log.error("Invalid number of arguments. Expected 2, got " + args.length);
                log.info("Usage: java -jar app.jar -text myTextForConversion");
                log.info("Usage: java -jar app.jar -cli");
                return;
            }
            if(!Objects.equals(args[0], "-text")) {
                log.error("Invalid first argument. Expected '-text', got '" + args[0] + "'");
                log.info("Usage: java -jar app.jar -text myTextForConversion");
                log.info("Usage: java -jar app.jar -cli");
                return;
            }
            if(args[1].isEmpty()) {
                log.error("Input text cannot be empty.");
                log.info("Usage: java -jar app.jar -text myTextForConversion");
                return;
            }
            text = args[1];
        }
        try {
            String result = text2AsciiArt.convert(text);
            System.out.println(result);
        } catch (ConversionException e) {
            log.error(e);
        }
	}
	
}