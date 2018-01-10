/* Class: Task.java
 * Author: Bastian Graebener - G00340600 */
package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

// TODO: Auto-generated Javadoc
/**
 * A Task creates 'minHashes' from <code>Shingles</code>.
 * <p>
 * Every Task should be run in its own thread.
 *
 * @author Bastian Graebener
 */
public class Task implements Runnable {

	private BlockingQueue<Shingle> queue;
	private Map<Integer, List<Integer>> hashes;
	private CountDownLatch latch;
	private List<Integer> rands;

	/**
	 * Creates a new instance of a task.
	 * <p>
	 * A <code>Task</code> takes new <code>Shingle</code>s of the BlockingQueue until it encounters a
	 * <code>PoisonPill</code>. It then creates a List of 'minHashes' for every Shingle and computes the smallest value
	 * for every index in the List of minHashes.
	 *
	 * @param queue
	 *            the BlockingQueue containing the Shingles
	 * @param hashes
	 *            the Map with a List of minHashes for every document
	 * @param rands
	 *            the List of random integer values
	 * @param latch
	 *            the CountDownLatch used to count the number of finished tasks
	 */
	public Task(BlockingQueue<Shingle> queue, Map<Integer, List<Integer>> hashes,
			List<Integer> rands, CountDownLatch latch) {
		this.queue = queue;
		this.hashes = hashes;
		this.latch = latch;
		this.rands = rands;
	}

	/**
	 * When a Task is submitted to a thread and executed new <code>Shingle</code>s are taken from the
	 * <code>BlockingQueue</code> populated by the <code>FileParser</code>. If the current Shingle is an instance of
	 * <code>PoisonPill</code> the <code>CountDownLatch</code> is counted down and the operation terminates.
	 * <p>
	 * For every Shingle a List of 'minHashes' is calculated by performing a XOR bitwise operation with the Shingles'
	 * hash value and every random integer in the List of random integers provided by the <code>Consumer</code>.
	 * This List is then merged with the List of previously computed 'minHashes' for the associated document.
	 */
	@Override
	public void run() {

		while (true) {

			try {
				Shingle next = queue.take();

				if (next instanceof PoisonShingle) {
					latch.countDown();
					return;
				}

				List<Integer> minHashes = rands.stream().map(rand -> rand ^ next.getHash())
						.collect(Collectors.toList());
				hashes.merge(next.getDocId(), minHashes, (a, b) -> this.computeMinList(a, b));

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Iterates over two <code>List</code>s of Integers and compares both Integers at one index. The smaller one of
	 * the two Integers is stored in a new <code>List</code> at the same index as the two original Integers.
	 * <p>
	 *
	 * @param a
	 *            the first List containing Integers
	 * @param b
	 *            the second List containing Integers
	 * @return the list with the smallest elements per index
	 */
	private List<Integer> computeMinList(List<Integer> a, List<Integer> b) {

		List<Integer> list = new ArrayList<>();

		if (a.size() != b.size()) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < a.size(); i++) {
			int min = a.get(i) < b.get(i) ? a.get(i) : b.get(i);
			list.add(min);
		}

		return list;
	}

}
