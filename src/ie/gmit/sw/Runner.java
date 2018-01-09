package ie.gmit.sw;

import java.nio.file.*;
import java.util.*;

/**
 *
 * @author Basti
 */
public class Runner {

	// private int numOfDocs = 0;
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

		// new Comparer().start();
	}

	public void showMenu() {

		System.out.println("******************Welcome to the Document Comparer******************");
		System.out.println("--------------------------------------------------------------------");
		System.out.println();

		// do {
		// System.out.print("Please enter the number of documents you wish to compare (min 2): ");
		// numOfDocs = scanner.nextInt();
		// } while (numOfDocs < 2);

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

		String choice;
		do {
			System.out.println("Do you want to use the default settings? (y\\n)");
			choice = scanner.next();
		} while (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n"));

		if (!choice.equalsIgnoreCase("y")) {
			getSettings();
		}

		Comparer comparer = new Comparer(numOfWorkers, numMinHashes, 2, shingleSize, paths);

		comparer.start();

	}

	private void showDefaultSettings() {
		System.out.println("\nDefault settings: ");
		System.out.println("Shingle size: " + shingleSize);
		System.out.println("Number of minhashes: " + numMinHashes);
		System.out.println("Number of workers: " + numOfWorkers);
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
