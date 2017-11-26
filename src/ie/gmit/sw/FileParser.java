package ie.gmit.sw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Basti
 */
public class FileParser implements Parser<List<Shingle>> {

	private Path fileLocation;
	private BlockingQueue<Shingle> shingleQueue;
	private int shingleSize;

	private int docId;

	public FileParser(Path fileLocation, BlockingQueue<Shingle> shingleQueue, int shingleSize, int docId) {
		this.fileLocation = fileLocation;
		this.shingleQueue = shingleQueue;
		this.shingleSize = shingleSize;
		this.docId = docId;
	}

	@Override
	public List<Shingle> parse() {
		// String[] doc = new String(Files.readAllBytes(fileLocation)).toLowerCase().trim().replaceAll("(\\R|[^a-z])", " ").replaceAll("\\s{2,}", " ").split(" ");
		// while (i < doc.length) {
		// shingle = "";
		//
		// for (int j = 0; j < shingleSize && i < doc.length; j++, i++) {
		// shingle += doc[i] + " ";
		// }
		// buffer.add(shingle.trim());
		//
		// }
		// String[] doc = tmp.parallelStream().map(line -> line.toLowerCase().trim().replaceAll("(\\R|[^a-z])", " ").replaceAll("\\s{2,}", " ")).reduce((a, b) -> a + " " + b)
		// .get().split(" ");

		List<Shingle> buffer = new LinkedList<>();
		try {

			// List<String> tmp = Files.readAllLines(fileLocation);

			StringTokenizer tok = new StringTokenizer(new String(Files.readAllBytes(fileLocation)).toLowerCase().trim().replaceAll("(\\R|[^a-z])", " ").replaceAll("\\s{2,}", " "));

			while (tok.hasMoreTokens()) {
				String shingle = "";
				for (int j = 0; j < shingleSize && tok.hasMoreTokens(); j++) {
					shingle += tok.nextToken() + " ";
				}
				buffer.add(new Shingle(docId, shingle.trim().hashCode()));

				// shingleQueue.put(new Shingle(docId, shingle.trim().hashCode()));

			}

			// System.out.println(shingleQueue.size());
			// System.out.println(shingleQueue);

			// buffer.stream().limit(10).forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;

	}

	// @Override
	// public void run() {
	// parse();
	//
	// }

	@Override
	public List<Shingle> call() throws Exception {

		return parse();
	}

}
