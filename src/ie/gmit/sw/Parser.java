/* Class: Parser.java
 * Author: Bastian Graebener - G00340600 */
package ie.gmit.sw;

/**
 * The Parser interface should be implemented by any class whose instances are used to parse a source.
 *
 * @author Bastian Graebener
 */
public interface Parser {

	/**
	 * An object implementing the <code>Parser</code> interface must provide an implementation of the parse method. The
	 * source of the parsing must be defined in the implementing class.
	 * <p>
	 * It may take any action.
	 */
	void parse();

}
