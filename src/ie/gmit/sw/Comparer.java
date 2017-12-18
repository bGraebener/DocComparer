package ie.gmit.sw;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Comparer {

	private FileParser fileParser;
	private BlockingQueue<Shingle> shingleQueue;
	private List<Integer> rands;
	private int numOfWorkers;
	// private CountDownLatch latch;
	private Map<Integer, List<Integer>> hashes;
	private Calculator calculator;

	private int numOfHashes;
	private int numOfFiles;
	private int shingleSize;

	public static void main(String[] args) {
		new Comparer().start();
	}

	public Comparer() {
		shingleQueue = new LinkedBlockingQueue<>();
		numOfWorkers = Runtime.getRuntime().availableProcessors();
		numOfHashes = 300;
		numOfFiles = 2;
		shingleSize = 4;

		fileParser = new FileParser(Paths.get("res/WarAndPeace.txt"), shingleQueue, shingleSize, 0, numOfWorkers, numOfFiles);
		rands = new Random(0).ints(numOfHashes).boxed().collect(Collectors.toList());
		// latch = new CountDownLatch(numOfWorkers);
		hashes = new ConcurrentHashMap<>();
		hashes.put(0, new ArrayList<>(Collections.nCopies(numOfHashes, Integer.MAX_VALUE)));
		hashes.put(1, new ArrayList<>(Collections.nCopies(numOfHashes, Integer.MAX_VALUE)));
		calculator = new JaccardCalculator(hashes);
	}

	private void start() {

		ExecutorService service = Executors.newCachedThreadPool();

		long start = System.currentTimeMillis();

		System.out.println("start parsing file 1");

		service.submit(fileParser);

		fileParser = new FileParser(Paths.get("res/War.txt"), shingleQueue, shingleSize, 1, numOfWorkers, numOfFiles);

		System.out.println("start parsing file 2");
		service.submit(fileParser);

		System.out.println("start workers");

		// List<Callable<Object>> tasks = Stream.generate(() -> new Task(shingleQueue, hashes, rands, latch)).limit(numOfWorkers).map(Executors::callable)
		// .collect(Collectors.toList());

		new Consumer(shingleQueue, hashes, numOfWorkers, numOfHashes);

		System.out.println("Workers finished ...");

		try {
			// service.invokeAll(tasks);

			service.shutdown();
			// latch.await();
			service.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		double result = calculator.calculate();
		System.out.println(result);
		System.out.println("finished in " + (System.currentTimeMillis() - start));

	}

}
