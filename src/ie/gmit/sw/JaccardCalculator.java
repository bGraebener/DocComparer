/* Class: JaccardCalculator.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.util.*;

/**
 * The JaccardCalculator calculates the Jaccard Index from two <code>List</code> instances containing hash numbers.
 *
 * @author Bastian Graebener
 */
public class JaccardCalculator implements Calculator {

	private Map<Integer, List<Integer>> hashes;

	/**
	 * Creates a new instance of JaccardCalculator.
	 * <p>
	 *
	 * @param hashes
	 *            the Map containing the list of minHashes for every document
	 */
	public JaccardCalculator(Map<Integer, List<Integer>> hashes) {
		this.hashes = hashes;
	}

	/**
	 * Calculates the Jaccard Index for the two documents indicating a percentage of similarity between the two.
	 * <p>
	 * The Jaccard Index is calculated by dividing the number of equal elements from both sets by the sum of all
	 * elements minus the number of equal elements.
	 *
	 * <blockquote>
	 * <tt>J(A,B)=|A∩B|/|A∪B|
	 * or
	 * <tt>J(A,B)=|A∩B|/|A|+|B|-|A∩B|
	 * </blockquote>
	 *
	 * @return the Jaccard Index calculated for the two documents as percentage
	 */
	@Override
	public double calculate() {

		List<Integer> a = hashes.get(0);
		List<Integer> b = hashes.get(1);

		List<Integer> c = new ArrayList<>(a);

		c.retainAll(b);

		double jaccard = (double) c.size() / (a.size() + b.size() - c.size());

		return jaccard * 100;
	}

}
