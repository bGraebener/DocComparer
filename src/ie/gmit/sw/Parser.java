package ie.gmit.sw;

import java.util.concurrent.Callable;

/**
 *
 * @author Basti
 * @param <T>
 */
public interface Parser<T> extends Callable<T> {

	T parse();

}
