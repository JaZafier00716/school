package lab;

import java.io.FileNotFoundException;
import lab.base.BaseStructure;
import lab.cars.CarTasks;
import lab.regexp.Parser;

/**
 * Class <b>App</b> - main class
 *
 * @author Java I
 */
public class App {

	public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Launching Java programing test all parts.");
        BaseStructure.main(args);
        CarTasks.main(args);
        Parser.main(args);
	}
}
