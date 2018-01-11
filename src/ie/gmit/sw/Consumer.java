/* Class: Consumer.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Consumer class creates and maintains worker threads which consume the shingles produced by the
 * <code>FileParser</code> instances.
 * <p>
 * The <code>Consumer</code> provides a fixed thread pool with a default size of 200 threads or a custom value provided
 * by the user. All tasks are created at once and passed to the thread pool. They run until they encounter a
 * <code>Shingle</code> that is an instance of <code>PoisonPill</code>.
 * <p>
 * The Consumer itself can be run in its own thread as it implements the <code>Runnable</code> interface.
 */
public class Consumer implements Runnable {

	private BlockingQueue<Shingle> queue;
	private Map<Integer, List<Integer>> hashes;
	private CountDownLatch latch;
	private List<Integer> rands;
	private ExecutorService service;
	// private int maxWorkers;

	private Settings settings;

	/**
	 * Instantiates a new <code>Consumer</code>.
	 * <p>
	 * A Consumer maintains a thread pool with the number of worker threads specified by the default value or by the
	 * user. A <code>CountDownLatch</code> is used to keep track of <code>Task</code> instances that are finished with
	 * their operations. It creates and provides the list of random numbers used by the Task instances to calculate the
	 * minHashes.
	 *
	 * @param queue
	 *            the BlockingQueue that contains the <code>Shingle</code> instances
	 * @param hashes
	 *            a <code>Map</code> which contains a List of hashes per document
	 * @param maxWorkers
	 *            the max number of worker threads
	 * @param numOfHashes
	 *            the number of hashes
	 */
	// public Consumer(BlockingQueue<Shingle> queue, Map<Integer, List<Integer>> hashes,
	// int maxWorkers, int numOfHashes) {
	// this.queue = queue;
	// this.hashes = hashes;
	// this.maxWorkers = maxWorkers;
	// this.latch = new CountDownLatch(maxWorkers);
	//
	// this.rands = new Random(0).ints(numOfHashes).boxed().collect(Collectors.toList());
	// service = Executors.newFixedThreadPool(maxWorkers);
	//
	// run();
	//
	// }

	public Consumer(BlockingQueue<Shingle> queue, Map<Integer, List<Integer>> hashes,
			Settings settings) {
		this.settings = settings;
		this.queue = queue;
		this.hashes = hashes;
		this.latch = new CountDownLatch(settings.getNumOfThreads());

		this.rands = new Random(0).ints(settings.getNumOfHashes()).boxed()
				.collect(Collectors.toList());
		service = Executors.newFixedThreadPool(settings.getNumOfThreads());

		// run();
	}

	/**
	 * When the <code>Consumer</code> is executed on a thread, a <code>List</code> of <code>Task</code> instances is
	 * created.
	 * This List is passed to an <code>ExecutorService</code> and invoked immediately. The Tasks need to ensure to count
	 * down the <code>CountDownLatch</code> provided by the Consumer or otherwise the Consumer can not finish.
	 *
	 */
	@Override
	public void run() {

		System.out.println(settings);

		List<Callable<Object>> tasks = Stream.generate(() -> new Task(queue, hashes, rands, latch))
				.limit(settings.getNumOfThreads()).map(Executors::callable)
				.collect(Collectors.toList());

		try {
			service.invokeAll(tasks);

			service.shutdown();
			latch.await();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
