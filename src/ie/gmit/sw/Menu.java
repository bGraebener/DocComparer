/* Class: Menu.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.nio.file.*;
import java.util.Scanner;

/**
 * Provides a command line menu to the user and sets values for settings needed to calculate the jaccard index.
 */
public class Menu {

	private Scanner scanner;
	private Settings settings;

	/**
	 * Creates a new Menu instance.
	 * <p>
	 * Provides the main menu for the application asking the user to enter the number of documents, their location on
	 * the file system and settings needed to calculate the jaccard index.
	 */
	public Menu() {
		scanner = new Scanner(System.in);
		settings = new Settings();
	}

	/**
	 * Shows the main application menu.
	 * <p>
	 * Asks the user for the amount of document they wish to compare and their location on the file system. Checks
	 * whether the files exist and asks for a location as long as no valid file path is entered.
	 *
	 * @return the settings entered by the user or default settings
	 */
	public Settings showMenu() {

		System.out
				.println("****************** Welcome to the Document Comparer ******************");
		System.out
				.println("----------------------------------------------------------------------");
		System.out.println();

		System.out.println("Instructions:");
		System.out.println(
				"This application compares a document against other documents\nand calculates the similarity of the documents using the Jaccard Index.\n"
						+ "\nYou can enter the number of documents you wish to compare and"
						+ "\nset options like the size of the shingles, the number of threads and\nhash values or use default values.\n");

		int numOfFiles;

		do {
			System.out.print("Please enter the number of document you wish to compare (min. 2): ");
			numOfFiles = scanner.nextInt();
		} while (numOfFiles < 2);

		settings.setNumOfFiles(numOfFiles);

		getOriginalDocumentLocation();

		getOtherDocumentLocations(numOfFiles);

		showDefaultSettings();

		chooseSettings();

		return settings;
	}

	/**
	 * Gets the locations for the other documents, checks if they exist and adds them to the list of locations in the
	 * settings.
	 *
	 * @param numOfFiles
	 *            the number of files the user wishes to enter
	 */
	private void getOtherDocumentLocations(int numOfFiles) {
		Path path;
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
	}

	/**
	 * Gets the location for the original document and checks if it exists. Adds it to the list of file locations in the
	 * settings.
	 */
	private void getOriginalDocumentLocation() {
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
		System.out.println("\nDefault settings:");
		// System.out.println("----------------");
		System.out.println("\nShingle size: " + settings.getShingleSize());
		System.out.println("Number of minHashes: " + settings.getNumOfHashes());
		System.out.println("Number of worker threads: " + settings.getNumOfThreads());
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
