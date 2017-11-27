package ie.gmit.sw;

import java.util.List;
import java.util.Map;

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

		return 0;
	}

}
