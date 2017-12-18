package ie.gmit.sw;

import java.util.*;

/**
 *
 * @author Basti
 */
public class JaccardCalculator implements Calculator {

	private Map<Integer, List<Integer>> hashes;

	public JaccardCalculator(Map<Integer, List<Integer>> hashes) {
		this.hashes = hashes;
	}

	@Override
	public double calculate() {

		List<Integer> a = hashes.get(0);
		List<Integer> b = hashes.get(1);

		List<Integer> c = new ArrayList<>(a);

		c.retainAll(b);

		System.out.println("c: " + c.size());

		double jaccard = (double) c.size() / (a.size() + b.size() - c.size());

		return jaccard;
	}

}
