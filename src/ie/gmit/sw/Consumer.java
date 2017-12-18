package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Basti
 */
public class Consumer implements Runnable {

	private BlockingQueue<Shingle> queue;
	private Map<Integer, List<Integer>> hashes;
	private CountDownLatch latch;
	private List<Integer> rands;

	private ExecutorService service;

	private int maxWorkers;
	// private int numOfHashes;

	public Consumer(BlockingQueue<Shingle> queue, Map<Integer, List<Integer>> hashes, int maxWorkers, int numOfHashes) {
		this.queue = queue;
		this.hashes = hashes;
		this.maxWorkers = maxWorkers;
		this.latch = new CountDownLatch(maxWorkers);
		// this.numOfHashes = numOfHashes;

		this.rands = new Random(0).ints(numOfHashes).boxed().collect(Collectors.toList());
		service = Executors.newFixedThreadPool(maxWorkers);

		run();

	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run() */
	@Override
	public void run() {
		List<Callable<Object>> tasks = Stream.generate(() -> new Task(queue, hashes, rands, latch)).limit(maxWorkers).map(Executors::callable).collect(Collectors.toList());

		try {
			service.invokeAll(tasks);

			service.shutdown();
			latch.await();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
