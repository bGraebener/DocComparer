/* Class: JaccardCalculator.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.util.*;

// TODO Javadocs
/**
 * The JaccardCalculator calculates the Jaccard Index from <code>List</code> instances containing hash numbers.
 *
 * @author Bastian Graebener
 */
public class JaccardCalculator implements Calculator {

	private Map<Integer, List<Integer>> hashes;

	/**
	 * Creates a new instance of JaccardCalculator.
	 * <p>
	 * The Jaccard index of two documents is calculated by dividing the number of common elements by the number of all
	 * elements minus the common elements.
	 *
	 * @param hashes
	 *            the Map containing the list of minHashes for every document
	 */
	public JaccardCalculator(Map<Integer, List<Integer>> hashes) {
		this.hashes = hashes;
	}

	/**
	 * Calculates the Jaccard Index for all documents with the original document. The index indicates a percentage of
	 * similarity between two documents.
	 * <p>
	 * The Jaccard Index is calculated by dividing the number of equal elements from both sets by the sum of all
	 * elements minus the number of equal elements.
	 *
	 * <blockquote>
	 * <tt>J(A,B)=|A∩B|/|A∪B|</tt>
	 * or
	 * <tt>J(A,B)=|A∩B|/|A|+|B|-|A∩B|</tt>
	 * </blockquote>
	 */
	@Override
	public void calculate() {

		List<Integer> tmp;
		List<Integer> original = hashes.remove(0);

		Set<Integer> keys = hashes.keySet();

		for (int key : keys) {

			List<Integer> other = hashes.get(key);
			tmp = new ArrayList<>(original);

			tmp.retainAll(other);

			double jaccard = (double) tmp.size() / (original.size() + other.size() - tmp.size());
			System.out.printf(
					"\nThe calculated similarity for the original document and document %d is: %.2f%%",
					key + 1, jaccard * 100);
		}
	}

}
