/* Class: Runner.java
 * Author: Bastian Graebener - G00340600 */
package ie.gmit.sw;

/**
 * The main entry point to the application.
 *
 * @author Bastian Graebener
 */
public class Runner {

	public static void main(String[] args) {
		Settings settings = new Menu().showMenu();
		Comparer comparer = new Comparer(settings);

		comparer.compareDocuments();

	}

}
