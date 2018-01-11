/* Class: MinHasher.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.*;

// TODO: Auto-generated Javadoc
/**
 * The MinHasher parses documents and calculates a <code>List</code> of minHashes for each document.
 *
 * @author Bastian Graebener
 */
public class MinHasher {

	private BlockingQueue<Shingle> shingleQueue;
	private Map<Integer, List<Integer>> hashes;
	private ExecutorService service;
	private List<FileParser> parsers;
	private Settings settings;

	/**
	 * Creates a new instance of a <code>MinHasher</code>.
	 * <p>
	 * A MinHasher maintains a <code>BlockingQueue</code>
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
	 * Initialises a <code>FileParser</code> instance and a <code>List</code> of minHashes for every document.
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
	 * Calculates minHashes for all documents.
	 * <p>
	 * All <code>FileParser</code>s producing the <code>Shingle</code>s and the <code>Consumer</code> processing the
	 * Shingles run on their own thread.
	 *
	 * @return the map containing all list of minHashes for all documents
	 */
	public Map<Integer, List<Integer>> calculateMinHashes() {

		System.out.println("\nStarting to calculate the Jaccard Index!");

		submitFileParsers();

		System.out.println("\nStarting worker threads...");
		service.execute(new Consumer(shingleQueue, hashes, settings));

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
	 * Submits all <code>FileParser</code>s to the thread pool.
	 */
	private void submitFileParsers() {

		parsers.forEach(parser -> {
			System.out.println("Parsing file " + (parser.getDocId() + 1) + "...");
			service.execute(parser);
		});
	}
}
