/* Class: Consumer.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Auto-generated Javadoc
/**
 * The Class Consumer.
 */
public class Consumer implements Runnable {

	private BlockingQueue<Shingle> queue;
	private Map<Integer, List<Integer>> hashes;
	private CountDownLatch latch;
	private List<Integer> rands;

	private ExecutorService service;

	private int maxWorkers;

	/**
	 * Instantiates a new consumer.
	 *
	 * <p>
	 *
	 * </p>
	 *
	 * @param queue
	 *            the queue
	 * @param hashes
	 *            the hashes
	 * @param maxWorkers
	 *            the max workers
	 * @param numOfHashes
	 *            the num of hashes
	 */
	public Consumer(BlockingQueue<Shingle> queue, Map<Integer, List<Integer>> hashes,
			int maxWorkers, int numOfHashes) {
		this.queue = queue;
		this.hashes = hashes;
		this.maxWorkers = maxWorkers;
		this.latch = new CountDownLatch(maxWorkers);

		this.rands = new Random(0).ints(numOfHashes).boxed().collect(Collectors.toList());
		service = Executors.newFixedThreadPool(maxWorkers);

		run();

	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run() */
	@Override
	public void run() {

		// int docCount = 2;

		List<Callable<Object>> tasks = Stream.generate(() -> new Task(queue, hashes, rands, latch))
				.limit(maxWorkers).map(Executors::callable).collect(Collectors.toList());

		// while (docCount > 0) {
		// try {
		// Shingle nextShingle = queue.take();
		//
		// if (nextShingle instanceof PoisonShingle) {
		// docCount--;
		// } else {
		//
		// service.submit(new Task(queue, hashes, rands, latch));
		// }
		//
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		//
		// }
		try {
			service.invokeAll(tasks);

			service.shutdown();
			latch.await();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
