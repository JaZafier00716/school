package lab;

import lab.cleaning.CleaningBot;
import lab.garden.Gardenner;
import lab.recepies.Chef;

/**
 * Class <b>App</b> - extends class Application and it is an entry point of the
 * program
 * 
 * @author Java I
 */
public class App {

	public static void main(String[] args) {
		System.out.println("Application lauched");

		DatabaseControl.startDBWebServer();
		
		new CleaningBot().run();
		new Gardenner().run();
		new Chef().run();

		DatabaseControl.waitForKeyAndStopDBWebServer();
	}
	
}