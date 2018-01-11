/* Class: Menu.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.nio.file.*;
import java.util.Scanner;

/**
 * The main entry point to the application.
 * <p>
 * Provides a command line menu to the user and sets default values for settings needed by the jaccard index calculator.
 */
public class Menu {

	private Scanner scanner;
	private Settings settings;

	/**
	 * Instantiates a new Menu.
	 *
	 * <p>
	 * Sets default values for the shingle sizes, number of minHashes and the number of worker threads.
	 */
	public Menu() {
		scanner = new Scanner(System.in);
		settings = new Settings();
	}

	/**
	 * Shows the main application menu.
	 *
	 * <p>
	 * Asks the user for the location for two documents on the file system. Checks whether the files exist on the file
	 * system and asks the user for a location as long as no valid file path is entered.
	 */
	public Settings showMenu() {

		System.out
				.println("****************** Welcome to the Document Comparer ******************");
		System.out
				.println("----------------------------------------------------------------------");
		System.out.println();

		// TODO display instructions

		int numOfFiles;
		Path path;

		do {
			System.out.print("Please enter the number of document you wish to compare (min. 2): ");
			numOfFiles = scanner.nextInt();
		} while (numOfFiles < 2);

		settings.setNumOfFiles(numOfFiles);

		setOriginalDocument();

		for (int i = 1; i < numOfFiles; i++) {

			System.out.print("Please enter the path to the " + (i + 1) + ". document: ");

			path = Paths.get(scanner.next());

			if (!Files.exists(path) || Files.isDirectory(path)) {
				System.out.println("Couldn't find file! Please try again!");
				i--;
				continue;
			}

			settings.getFileLocations().add(path);
		}

		showDefaultSettings();

		chooseSettings();

		return settings;
	}

	/**
	 * Sets the original document and check.
	 *
	 * <p>
	 */
	private void setOriginalDocument() {
		Path path;

		do {
			System.out.print("Please enter the path to the original document: ");
			path = Paths.get(scanner.next());
			if (!Files.exists(path) || Files.isDirectory(path)) {
				System.out.println("Couldn't find file! Please try again!");
				continue;
			}

		} while (!Files.exists(path) || Files.isDirectory(path));

		settings.getFileLocations().add(path);
	}

	/**
	 * Asks the user whether to use default settings or gets the custom settings.
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
		System.out.println("\n\tShingle size: " + settings.getShingleSize());
		System.out.println("\tNumber of minHashes: " + settings.getNumOfHashes());
		System.out.println("\tNumber of worker threads: " + settings.getNumOfThreads());
	}

	/**
	 * Sets the custom settings from the user input.
	 */
	private void getSettings() {
		System.out.print("Please enter shingle size: ");
		settings.setShingleSize(scanner.nextInt());
		System.out.print("Please enter number of minhashes: ");
		settings.setNumOfHashes(scanner.nextInt());
		System.out.print("Please enter number of worker threads: ");
		settings.setNumOfThreads(scanner.nextInt());
	}

}
