/* Class: Comparer.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Comparer.
 */
public class Comparer {

	// private FileParser fileParserOne;
	// private FileParser fileParserTwo;
	private BlockingQueue<Shingle> shingleQueue;
	// private int numOfWorkers;
	private Map<Integer, List<Integer>> hashes;
	// private int numOfHashes;
	private ExecutorService service;

	private List<FileParser> parsers;

	private Settings settings;

	/**
	 * Creates a new instance of a <code>Comparer</code>.
	 * <p>
	 *
	 * @param numOfWorkers
	 *            the number of worker threads
	 * @param numOfHashes
	 *            the number of random integer hashes
	 * @param numOfFiles
	 *            the number of documents being compared
	 * @param shingleSize
	 *            the number of words a shingle is made up of
	 * @param paths
	 *            the paths of the documents
	 */
	// public Comparer(int numOfWorkers, int numOfHashes, int numOfFiles, int shingleSize,
	// List<Path> paths) {
	// this.numOfWorkers = numOfWorkers;
	// this.numOfHashes = numOfHashes;
	//
	// shingleQueue = new LinkedBlockingQueue<>();
	//
	// hashes = new ConcurrentHashMap<>();
	// hashes.put(0, new ArrayList<>(Collections.nCopies(numOfHashes, Integer.MAX_VALUE)));
	// hashes.put(1, new ArrayList<>(Collections.nCopies(numOfHashes, Integer.MAX_VALUE)));
	//
	// fileParserOne = new FileParser(paths.get(0), shingleQueue, shingleSize, 0,
	// numOfWorkers / numOfFiles + 1);
	// fileParserTwo = new FileParser(paths.get(1), shingleQueue, shingleSize, 1,
	// numOfWorkers / numOfFiles + 1);
	//
	// service = Executors.newFixedThreadPool(numOfFiles + 1);
	//
	// }

	public Comparer(Settings settings) {
		this.settings = settings;
		shingleQueue = new LinkedBlockingQueue<>();
		hashes = new ConcurrentHashMap<>();
		parsers = new ArrayList<>();
		service = Executors.newFixedThreadPool(settings.getNumOfFiles());

		initialise();
	}

	private void initialise() {

		for (int i = 0; i < settings.getNumOfFiles(); i++) {
			hashes.put(i, new ArrayList<>(
					Collections.nCopies(settings.getNumOfHashes(), Integer.MAX_VALUE)));
			parsers.add(
					new FileParser(settings.getFileLocations().get(i), shingleQueue, i, settings));
		}

	}

	/**
	 * Starts the comparison of two documents.
	 * <p>
	 * Submits the <code>FileParsers</code> and the <code>Consumer</code> to the thread pool.
	 * Displays the calculated Jaccard Index as a percentage and time it took to finish the operation in milliseconds.
	 */
	public void compareDocuments() {

		long start = System.currentTimeMillis();
		System.out.println("\nStarting to calculate the Jaccard Index!");

		submitFileParsers();

		submitConsumer();

		try {
			service.shutdown();
			service.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		double result = new JaccardCalculator(hashes).calculate();

		System.out.printf("\nThe calculated similarity for the documents is: %.2f%%\n", result);
		System.out.println("The operation took " + (System.currentTimeMillis() - start)
				+ " milliseconds to complete.");

	}

	/**
	 * Submits the consumer to the thread pool.
	 * <p>
	 * Displays messages about start and end of the operation.
	 */
	private void submitConsumer() {
		System.out.println("\nStarting worker threads...");

		// service.submit(new Consumer(shingleQueue, hashes, numOfWorkers, numOfHashes));
		service.submit(new Consumer(shingleQueue, hashes, settings));

		System.out.println("Worker threads finished...");
	}

	/**
	 * Submits file parsers to the thread pool.
	 * <p>
	 * Displays messages about start of the operation.
	 */
	private void submitFileParsers() {

		// System.out.println("\nParsing file 1 ...");
		//
		// service.submit(fileParserOne);
		//
		// System.out.println("Parsing file 2 ...");
		// service.submit(fileParserTwo);

		parsers.forEach(parser -> {
			System.out.println("\nParsing file " + parser.getDocId() + " ...");
			service.submit(parser);
		});
	}

}
