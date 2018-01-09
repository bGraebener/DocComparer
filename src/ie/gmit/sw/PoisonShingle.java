/* Class: PoisonShingle.java
 * Author: Bastian Graebener - G00340600 */
package ie.gmit.sw;

/**
 * Used as a poison pill to signal the worker threads the end of a document.
 *
 * @author Bastian Graebener
 */
public class PoisonShingle extends Shingle {

	/**
	 * Instantiates a new poison shingle.
	 */
	public PoisonShingle() {
		super(0, 0);
	}

}
