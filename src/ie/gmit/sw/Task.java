/* Class: Task.java
 * Author: Bastian Graebener - G00340600 */
package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

// TODO: Auto-generated Javadoc
/**
 * The Class Task.
 *
 * @author Bastian Graebener
 */
public class Task implements Runnable {

	private BlockingQueue<Shingle> queue;
	private Map<Integer, List<Integer>> hashes;
	private CountDownLatch latch;
	private List<Integer> rands;

	/**
	 * Instantiates a new task.
	 *
	 * <p>
	 *
	 * </p>
	 *
	 * @param queue
	 *            the queue
	 * @param hashes
	 *            the hashes
	 * @param rands
	 *            the rands
	 * @param latch
	 *            the latch
	 */
	public Task(BlockingQueue<Shingle> queue, Map<Integer, List<Integer>> hashes,
			List<Integer> rands, CountDownLatch latch) {
		this.queue = queue;
		this.hashes = hashes;
		this.latch = latch;
		this.rands = rands;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run() */
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

				// System.out.println(hashes.get(next.getDocId()));

				// for (int i = 0; i < rands.size(); i++) {
				// int hash = next.getHash() ^ rands.get(i);
				//
				// synchronized (hashes) {
				//
				// if (hashes.get(next.getDocId()).get(i) > hash) {
				// hashes.get(next.getDocId()).set(i, hash);
				// }
				// }
				//
				// }

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Compute min list.
	 *
	 * <p>
	 *
	 * </p>
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the list
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
