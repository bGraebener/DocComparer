/* Class: MinHasher.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.*;

// TODO: Auto-generated Javadoc
public class MinHasher {

	private BlockingQueue<Shingle> shingleQueue;
	private Map<Integer, List<Integer>> hashes;
	private ExecutorService service;
	private List<FileParser> parsers;
	private Settings settings;

	/**
	 * Creates a new instance of a <code>MinHasher</code>.
	 * <p>
	 *
	 * @param settings
	 *            settings used calculate the similarity of the documents
	 */
	public MinHasher(Settings settings) {
		this.settings = settings;
		shingleQueue = new LinkedBlockingQueue<>();
		hashes = new ConcurrentHashMap<>();
		parsers = new ArrayList<>();
		service = Executors.newFixedThreadPool(settings.getNumOfFiles());

		initialise();
	}

	/**
	 * Initialise.
	 *
	 */
	private void initialise() {

		for (int i = 0; i < settings.getNumOfFiles(); i++) {
			hashes.put(i, new ArrayList<>(
					Collections.nCopies(settings.getNumOfHashes(), Integer.MAX_VALUE)));
			parsers.add(
					new FileParser(settings.getFileLocations().get(i), shingleQueue, i, settings));
		}
	}

	/**
	 * Calculates minhashes for all documents.
	 *
	 * <p>
	 *
	 * @return the map
	 */
	public Map<Integer, List<Integer>> calculateMinHashes() {

		System.out.println("\nStarting to calculate the Jaccard Index!");

		submitFileParsers();

		System.out.println("\nStarting worker threads...");
		service.submit(new Consumer(shingleQueue, hashes, settings));

		try {
			service.shutdown();
			service.awaitTermination(1, TimeUnit.DAYS);
			System.out.println("Worker threads finished...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return hashes;
	}

	/**
	 * Submits file parsers to the thread pool.
	 * <p>
	 * Displays messages about start of the operation.
	 */
	private void submitFileParsers() {

		parsers.forEach(parser -> {
			System.out.println("Parsing file " + (parser.getDocId() + 1) + " ...");
			service.submit(parser);
		});
	}

}
