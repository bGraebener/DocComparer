/* Class: FileParser.java
 * Author: Bastian Graebener - G00340600 */

package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

/**
 * A <code>Parser</code> that takes a file as a source for the parsing and produces shingles for the
 * <code>Consumer</code> to consume.
 * <p>
 * A <code>FileParser</code> uses a <code>BufferedReader</code> to read a text based file line-by-line. The lines are
 * split up into individual words and sanitized to only contain alpha-numerical characters. The words are combined to a
 * <code>Shingle</code> of user specified size. The Shingle is then put into a BlockingQueue for further processing by a
 * <code>Task</code>.
 * <p>
 * It implements <code>Runnable</code> so every parsing operation can run on its own thread.
 */
public class FileParser implements Parser, Runnable {

	private Path fileLocation;
	private BlockingQueue<Shingle> shingleQueue;
	// private int shingleSize;
	// private int numOfWorkers;
	private int docId;
	// private int numOfFiles;
	// private int numOfPoisonPills;

	private Settings settings;

	/**
	 * Creates a new instance of a <code>FileParser</code> that creates Shingles from a text based file and puts them in
	 * a BlockingQueue.
	 *
	 * @param fileLocation
	 *            the file location of the source to be parsed
	 * @param shingleQueue
	 *            a <code>BlockinQueue</code> that holds Shingles to be processed by a <code>Task</code>
	 * @param shingleSize
	 *            the amount of words per shingle
	 * @param docId
	 *            the document id of the document processed by this FileParser
	 * @param numOfWorkers
	 *            the number of worker threads that take Shingles of the BlockinQueue
	 * @param numOfFiles
	 *            the total number of files that are processed
	 */
	// public FileParser(Path fileLocation, BlockingQueue<Shingle> shingleQueue, int shingleSize,
	// int docId, int numOfPoisonPills) {
	// this.fileLocation = fileLocation;
	// this.shingleQueue = shingleQueue;
	// this.shingleSize = shingleSize;
	// this.docId = docId;
	// this.numOfPoisonPills = numOfPoisonPills;
	// }

	public FileParser(Path fileLocation, BlockingQueue<Shingle> shingleQueue, int docId,
			Settings settings) {
		this.settings = settings;
		this.docId = docId;
		this.fileLocation = fileLocation;
		this.shingleQueue = shingleQueue;
	}

	/**
	 * Creates a <code>BufferedReader</code> from a <code>Path</code>. The BufferedReader reads the source file line by
	 * line. The line of Strings is split into words, Shingles of user specified size are created and put into a
	 * <code>BlockingQueue</code>. Finally poison pills are added to the BlockingQueue to signal the end of a file and
	 * no further shingles are put into the Queue.
	 */
	@Override
	public void parse() {

		long start = System.currentTimeMillis();

		Deque<String> buffer = null;
		try (BufferedReader reader = Files.newBufferedReader(fileLocation)) {

			String line;
			while ((line = reader.readLine()) != null) {

				buffer = splitLine(line);

				while (buffer.size() >= settings.getShingleSize()) {
					createShingle(buffer);
				}
			}

			// create final shingle from left over words in buffer
			flushBuffer(buffer);

			addPoisonPills();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Completed parsing document " + docId + " in "
				+ (System.currentTimeMillis() - start) + " milliseconds.");
	}

	/**
	 * Splits a <code>String</code> using whitespace as a delimiter. Sanitises the tokens to only contain
	 * lower-case alpha-numerical characters and adds the single tokens to a <code>Deque</code>.
	 *
	 * @param line
	 *            a single String from a text based file
	 * @return the Deque containing the sanitised words in the line
	 */
	private Deque<String> splitLine(String line) {
		Deque<String> buffer = new LinkedList<>();

		Stream.of(line.split(" ")).map(word -> word.toLowerCase().replaceAll("\\W", " ").trim())
				.filter(word -> !word.isEmpty()).forEach(buffer::add);
		return buffer;
	}

	/**
	 * Creates a single <code>Shingle</code> by concatenating strings from a provided Deque and delimiting them
	 * with a whitespace. Then attempts to put it into a <code>BlockinQueue</code>.
	 *
	 * @param buffer
	 *            the Deque containing single words
	 */
	private void createShingle(Deque<String> buffer) {

		StringBuilder tmp = new StringBuilder();

		for (int i = 0; i < settings.getShingleSize(); i++) {
			tmp.append(buffer.removeFirst() + " ");
		}

		try {
			shingleQueue.put(new Shingle(docId, tmp.toString().hashCode()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Flushes the <code>LinkedList</code> used as a buffer after the last line was read from the source file.
	 * <p>
	 * Concatenates the remaining words in the buffer to a String with a whitespace as delimiter, converts it to a
	 * Shingle and puts it into a <code>BlockingQueue</code>.
	 *
	 * @param buffer
	 *            the Deque used as a buffer of Strings
	 */
	private void flushBuffer(Deque<String> buffer) {
		if (buffer.size() > 0) {
			int lastHash = buffer.stream().reduce((a, b) -> a + " " + b).get().hashCode();
			try {
				shingleQueue.put(new Shingle(docId, lastHash));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Adds the poison pills to the end of the <code>BlockingQueue</code> to signal the worker threads to stop.
	 * <p>
	 * The worker threads check each <code>Shingle</code> they take of the BlockingQueue. If the Shingle is an instance
	 * of <code>PoisonShingle</code> the thread stops its operation.
	 * <p>
	 * The number of poison pills depends on the amount of worker threads currently active and the amount of files being
	 * read.
	 */
	private void addPoisonPills() {
		for (int i = 0; i < settings.getNumOfPoisonPills(); i++) {
			try {
				shingleQueue.put(new PoisonShingle());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call() */
	@Override
	public void run() {
		parse();
	}

	public int getDocId() {
		return docId;
	}

	@Override
	public String toString() {
		return "FileParser [" + (fileLocation != null ? "fileLocation=" + fileLocation + ", " : "")
				+ "docId=" + docId + "]";
	}

}
