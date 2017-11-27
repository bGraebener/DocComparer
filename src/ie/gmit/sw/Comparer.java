package ie.gmit.sw;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Comparer {

	private FileParser fileParser;
	private BlockingQueue<Shingle> shingleQueue;
	private List<Integer> rands;
	private int numOfWorkers;
	private CountDownLatch latch;
	private Map<Integer, List<Integer>> hashes;
	private Calculator calculator;

	private int numOfHashes;
	private int numOfFiles;

	public static void main(String[] args) {
		new Comparer().start();
	}

	public Comparer() {
		shingleQueue = new LinkedBlockingQueue<>();
		numOfWorkers = 4;
		numOfHashes = 300;
		numOfFiles = 2;

		fileParser = new FileParser(Paths.get("res/WarAndPeace.txt"), shingleQueue, 4, 0, numOfWorkers, numOfFiles);
		rands = new Random().ints(numOfHashes).boxed().collect(Collectors.toList());
		latch = new CountDownLatch(numOfWorkers);

		// List<Integer> docOneHashes = IntStream.generate(() -> Integer.MAX_VALUE).limit(numOfHashes).boxed().collect(Collectors.toList());
		// List<Integer> docTwoHashes = IntStream.generate(() -> Integer.MAX_VALUE).limit(numOfHashes).boxed().collect(Collectors.toList());

		hashes = new ConcurrentHashMap<>();
		// hashes.put(0, docOneHashes);
		// hashes.put(1, docTwoHashes);

		calculator = new JaccardCalculator(hashes);
	}

	private void start() {

		ExecutorService service = Executors.newCachedThreadPool();

		long start = System.currentTimeMillis();

		System.out.println("start parsing file 1");

		service.submit(fileParser);

		fileParser = new FileParser(Paths.get("res/WarAndPeace.txt"), shingleQueue, 4, 1, numOfWorkers, numOfFiles);

		System.out.println("start parsing file 2");
		service.submit(fileParser);

		System.out.println("start workers");

		// for (int i = 0; i < numOfWorkers; i++) {
		// service.submit(new Task(shingleQueue, hashes, rands, latch));
		// }

		List<Callable<Object>> tasks = Stream.generate(() -> new Task(shingleQueue, hashes, rands, latch)).limit(numOfWorkers).map(Executors::callable)
				.collect(Collectors.toList());

		try {
			service.invokeAll(tasks);

			service.shutdown();
			latch.await();
			// service.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("finished in " + (System.currentTimeMillis() - start));

		// System.out.println(hashes.get(0));
		// System.out.println(hashes.get(1));

	}

}
