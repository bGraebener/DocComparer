package ie.gmit.sw;

import java.nio.file.*;
import java.util.*;

/**
 *
 * @author Basti
 */
public class Runner {

	private List<Path> paths = new ArrayList<>();

	private Scanner scanner;

	private int shingleSize;
	private int numMinHashes;
	private int numOfWorkers;

	public Runner() {

		shingleSize = 4;
		numMinHashes = 200;
		numOfWorkers = 100;

		scanner = new Scanner(System.in);

	}

	public static void main(String[] args) {

		new Runner().showMenu();
	}

	public void showMenu() {

		System.out.println("******************Welcome to the Document Comparer******************");
		System.out.println("--------------------------------------------------------------------");
		System.out.println();

		// get paths for two documents
		for (int i = 0; i < 2; i++) {

			System.out.print("Please enter the path to the " + (i + 1) + ". document: ");

			Path path = Paths.get(scanner.next());

			if (!Files.exists(path)) {
				System.out.println("Couldn't find path! Please try again!");
				i--;
				continue;
			}

			paths.add(path);
		}

		showDefaultSettings();

		chooseSettings();

		Comparer comparer = new Comparer(numOfWorkers, numMinHashes, 2, shingleSize, paths);

		comparer.start();

	}

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

	private void showDefaultSettings() {
		System.out.println("\nDefault settings");
		System.out.println("----------------");
		System.out.println("\n\tShingle size: " + shingleSize);
		System.out.println("\tNumber of minhashes: " + numMinHashes);
		System.out.println("\tNumber of workers: " + numOfWorkers);
	}

	private void getSettings() {
		System.out.print("Please enter shingle size: ");
		shingleSize = scanner.nextInt();
		System.out.print("Please enter number of minhashes: ");
		numMinHashes = scanner.nextInt();
		System.out.print("Please enter number of worker threads: ");
		numOfWorkers = scanner.nextInt();
	}

}
