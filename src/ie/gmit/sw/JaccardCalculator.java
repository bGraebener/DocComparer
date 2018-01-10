/* Class: JaccardCalculator.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * The JaccardCalculator calculates the Jaccard Index from two <code>List</code> instances of hash numbers.
 * <p>
 * A Map with two
 */
public class JaccardCalculator implements Calculator {

	private Map<Integer, List<Integer>> hashes;

	/**
	 * Creates a new instance of JaccardCalculator.
	 *
	 * <p>
	 *
	 * </p>
	 *
	 * @param hashes
	 *            the hashes
	 */
	public JaccardCalculator(Map<Integer, List<Integer>> hashes) {
		this.hashes = hashes;
	}

	/* (non-Javadoc)
	 * @see ie.gmit.sw.Calculator#calculate() */
	@Override
	public double calculate() {

		List<Integer> a = hashes.get(0);
		List<Integer> b = hashes.get(1);

		List<Integer> c = new ArrayList<>(a);

		c.retainAll(b);

		// System.out.println("c: " + c.size());

		double jaccard = (double) c.size() / (a.size() + b.size() - c.size());

		return jaccard * 100;
	}

}
