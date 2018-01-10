/* Class: Comparer.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Comparer.
 */
public class Comparer {

	private FileParser fileParserOne;
	private FileParser fileParserTwo;
	private BlockingQueue<Shingle> shingleQueue;
	private int numOfWorkers;
	private Map<Integer, List<Integer>> hashes;
	private Calculator calculator;

	private int numOfHashes;

	/**
	 * Instantiates a new comparer.
	 *
	 * <p>
	 *
	 * </p>
	 *
	 * @param numOfWorkers
	 *            the num of workers
	 * @param numOfHashes
	 *            the num of hashes
	 * @param numOfFiles
	 *            the num of files
	 * @param shingleSize
	 *            the shingle size
	 * @param paths
	 *            the paths
	 */
	public Comparer(int numOfWorkers, int numOfHashes, int numOfFiles, int shingleSize,
			List<Path> paths) {
		this.numOfWorkers = numOfWorkers;
		this.numOfHashes = numOfHashes;

		shingleQueue = new LinkedBlockingQueue<>();

		hashes = new ConcurrentHashMap<>();
		hashes.put(0, new ArrayList<>(Collections.nCopies(numOfHashes, Integer.MAX_VALUE)));
		hashes.put(1, new ArrayList<>(Collections.nCopies(numOfHashes, Integer.MAX_VALUE)));
		calculator = new JaccardCalculator(hashes);

		fileParserOne = new FileParser(paths.get(0), shingleQueue, shingleSize, 0,
				numOfWorkers / numOfFiles + 1);
		fileParserTwo = new FileParser(paths.get(1), shingleQueue, shingleSize, 1,
				numOfWorkers / numOfFiles + 1);

	}

	/**
	 * Start.
	 *
	 * <p>
	 *
	 * </p>
	 */
	public void start() {

		ExecutorService service = Executors.newCachedThreadPool();

		long start = System.currentTimeMillis();

		System.out.println("\nStarting to calculate the Jaccard Index!");

		System.out.println("\nParsing file 1 ...");

		service.submit(fileParserOne);

		System.out.println("Parsing file 2 ...");
		service.submit(fileParserTwo);

		System.out.println("\nStarting worker threads...");

		service.submit(new Consumer(shingleQueue, hashes, numOfWorkers, numOfHashes));

		System.out.println("Worker threads finished...");

		try {
			service.shutdown();
			service.awaitTermination(1, TimeUnit.DAYS);
			// System.out.println("comparer shutdown");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		double result = calculator.calculate();
		System.out.printf("\nThe calculated similarity for the documents is: %.2f%%\n", result);
		System.out.println("The operation took " + (System.currentTimeMillis() - start)
				+ " milliseconds to complete.");

	}

}
