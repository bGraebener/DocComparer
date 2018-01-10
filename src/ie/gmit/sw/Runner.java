/* Class: Runner.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.nio.file.*;
import java.util.*;

/**
 * The main entry point to the application.
 * <p>
 * Provides a command line menu to the user and sets default values for settings needed by the jaccard index calculator.
 */
public class Runner {

	private List<Path> paths;
	private Scanner scanner;
	private int shingleSize;
	private int numMinHashes;
	private int numOfWorkers;

	/**
	 * Instantiates a new runner.
	 *
	 * <p>
	 * Sets default values for the shingle sizes, number of minHashes and the number of worker threads.
	 */
	public Runner() {

		shingleSize = 4;
		numMinHashes = 200;
		numOfWorkers = 100;

		scanner = new Scanner(System.in);
		paths = new ArrayList<>();
	}

	public static void main(String[] args) {
		new Runner().showMenu();
	}

	/**
	 * Shows the main application menu.
	 *
	 * <p>
	 * Asks the user for the location for two documents on the file system. Checks whether the files exist on the file
	 * system and asks the user for a location as long as no valid file path is entered.
	 */
	public void showMenu() {

		System.out
				.println("****************** Welcome to the Document Comparer ******************");
		System.out
				.println("----------------------------------------------------------------------");
		System.out.println();

		// get paths for two documents
		for (int i = 0; i < 2; i++) {

			System.out.print("Please enter the path to the " + (i + 1) + ". document: ");

			Path path = Paths.get(scanner.next());

			if (!Files.exists(path) || Files.isDirectory(path)) {
				System.out.println("Couldn't find file! Please try again!");
				i--;
				continue;
			}

			paths.add(path);
		}

		showDefaultSettings();

		chooseSettings();

		Comparer comparer = new Comparer(numOfWorkers, numMinHashes, 2, shingleSize, paths);

		comparer.compareDocuments();

	}

	/**
	 * Asks the user whether to use default settings or not.
	 */
	private void chooseSettings() {
		String choice;
		do {
			System.out.print("\nDo you want to use the default settings? (y\\n): ");
			choice = scanner.next();
		} while (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n"));

		if (!choice.equalsIgnoreCase("y")) {
			getSettings();
		}
	}

	/**
	 * Shows the default settings.
	 */
	private void showDefaultSettings() {
		System.out.println("\nDefault settings");
		System.out.println("----------------");
		System.out.println("\n\tShingle size: " + shingleSize);
		System.out.println("\tNumber of minHashes: " + numMinHashes);
		System.out.println("\tNumber of worker threads: " + numOfWorkers);
	}

	/**
	 * Sets the custom settings from the user input.
	 */
	private void getSettings() {
		System.out.print("Please enter shingle size: ");
		shingleSize = scanner.nextInt();
		System.out.print("Please enter number of minhashes: ");
		numMinHashes = scanner.nextInt();
		System.out.print("Please enter number of worker threads: ");
		numOfWorkers = scanner.nextInt();
	}

}
