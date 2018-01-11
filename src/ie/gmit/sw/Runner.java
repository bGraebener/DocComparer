/* Class: Runner.java
 * Author: Bastian Graebener - G00340600 */
package ie.gmit.sw;

import java.util.List;
import java.util.Map;

/**
 * The main entry point to the application.
 *
 * @author Bastian Graebener
 */
public class Runner {

	public static void main(String[] args) {

		Settings settings = new Menu().showMenu();

		long start = System.currentTimeMillis();
		MinHasher comparer = new MinHasher(settings);

		Map<Integer, List<Integer>> hashes = comparer.calculateMinHashes();

		Calculator calculator = new JaccardCalculator(hashes);
		calculator.calculate();

		System.out.println("\nThe operation took " + (System.currentTimeMillis() - start)
				+ " milliseconds to complete.");

	}

}
