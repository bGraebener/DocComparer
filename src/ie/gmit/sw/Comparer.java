package ie.gmit.sw;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

public class Comparer {

	private FileParser fileParserOne;
	private FileParser fileParserTwo;
	private BlockingQueue<Shingle> shingleQueue;
	private int numOfWorkers;
	private Map<Integer, List<Integer>> hashes;
	private Calculator calculator;

	private int numOfHashes;

	public Comparer(int numOfWorkers, int numOfHashes, int numOfFiles, int shingleSize, List<Path> paths) {
		this.numOfWorkers = numOfWorkers;
		this.numOfHashes = numOfHashes;

		shingleQueue = new LinkedBlockingQueue<>();

		hashes = new ConcurrentHashMap<>();
		hashes.put(0, new ArrayList<>(Collections.nCopies(numOfHashes, Integer.MAX_VALUE)));
		hashes.put(1, new ArrayList<>(Collections.nCopies(numOfHashes, Integer.MAX_VALUE)));
		calculator = new JaccardCalculator(hashes);

		fileParserOne = new FileParser(paths.get(0), shingleQueue, shingleSize, 0, numOfWorkers, numOfFiles);
		fileParserTwo = new FileParser(paths.get(1), shingleQueue, shingleSize, 1, numOfWorkers, numOfFiles);

	}

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
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		double result = calculator.calculate();
		System.out.printf("\nThe calculated similarity for the documents is: %.2f%%\n", result);
		System.out.println("The operation took " + (System.currentTimeMillis() - start) + " milliseconds to complete.");

	}

}
