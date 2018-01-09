/* Class: FileParser.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

// TODO: Auto-generated Javadoc
/**
 * A <code>Parser</code> that takes a file as a source for the parsing.
 * <p>
 * <code>FileParser</code> uses a <code>BufferedReader</code> to read a text based file line-by-line. The lines are
 * split up into individual words and sanitized to only contain ASCII characters.
 * <p>
 * It implements <code>Runnable</code> so every parsing operation can run on its own thread.
 *
 *
 */
public class FileParser implements Parser, Runnable {

	private Path fileLocation;
	private BlockingQueue<Shingle> shingleQueue;
	private int shingleSize;
	private int numOfWorkers;
	private int docId;
	private int numOfFiles;

	/**
	 * Creates a new instance of a <code>FileParser</code>.
	 *
	 * <p>
	 *
	 * </p>
	 *
	 * @param fileLocation
	 *            the file location
	 * @param shingleQueue
	 *            the shingle queue
	 * @param shingleSize
	 *            the shingle size
	 * @param docId
	 *            the doc id
	 * @param numOfWorkers
	 *            the num of workers
	 * @param numOfFiles
	 *            the num of files
	 */
	public FileParser(Path fileLocation, BlockingQueue<Shingle> shingleQueue, int shingleSize,
			int docId, int numOfWorkers, int numOfFiles) {
		this.fileLocation = fileLocation;
		this.shingleQueue = shingleQueue;
		this.shingleSize = shingleSize;
		this.docId = docId;
		this.numOfWorkers = numOfWorkers;
		this.numOfFiles = numOfFiles;
	}

	/* (non-Javadoc)
	 * @see ie.gmit.sw.Parser#parse() */
	@Override
	public void parse() {

		long start = System.currentTimeMillis();

		LinkedList<String> buffer = new LinkedList<>();
		try (BufferedReader reader = Files.newBufferedReader(fileLocation)) {

			String line = "";
			while ((line = reader.readLine()) != null) {

				Stream.of(line.split(" "))
						.map(word -> word.toLowerCase().replaceAll("\\W", " ").trim())
						.filter(word -> !word.isEmpty()).forEach(buffer::add);

				while (buffer.size() >= shingleSize) {
					StringBuilder tmp = new StringBuilder();

					for (int i = 0; i < shingleSize; i++) {

						tmp.append(buffer.removeFirst() + " ");

					}

					shingleQueue.put(new Shingle(docId, tmp.toString().hashCode()));

				}

			}
			if (buffer.size() > 0) {

				int lastHash = buffer.stream().reduce((a, b) -> a + " " + b).get().hashCode();
				shingleQueue.put(new Shingle(docId, lastHash));
			}

			// add enough poison pills for all workers
			for (int i = 0; i < numOfWorkers / numOfFiles + 1; i++) {
				shingleQueue.put(new PoisonShingle());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Completed parsing document " + docId + " in "
				+ (System.currentTimeMillis() - start) + " milliseconds.");

	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call() */
	@Override
	public void run() {

		parse();
	}

}
