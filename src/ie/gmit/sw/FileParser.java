package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

/**
 *
 * @author Basti
 */
public class FileParser implements Parser<Void> {

	private Path fileLocation;
	private BlockingQueue<Shingle> shingleQueue;
	private int shingleSize;
	private int numOfWorkers;
	private int docId;
	private int numOfFiles;

	public FileParser(Path fileLocation, BlockingQueue<Shingle> shingleQueue, int shingleSize, int docId, int numOfWorkers, int numOfFiles) {
		this.fileLocation = fileLocation;
		this.shingleQueue = shingleQueue;
		this.shingleSize = shingleSize;
		this.docId = docId;
		this.numOfWorkers = numOfWorkers;
		this.numOfFiles = numOfFiles;
	}

	@Override
	public Void parse() {

		long start = System.currentTimeMillis();

		LinkedList<String> buffer = new LinkedList<>();
		try (BufferedReader reader = Files.newBufferedReader(fileLocation)) {

			String line = "";
			while ((line = reader.readLine()) != null) {

				Stream.of(line.split(" ")).map(word -> word.toLowerCase().replaceAll("\\W", " ").trim()).filter(word -> !word.isEmpty()).forEach(buffer::add);

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

			for (int i = 0; i < numOfWorkers / numOfFiles + 1; i++) {
				shingleQueue.put(new PoisonShingle());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Finished parsing " + docId + " in: " + (System.currentTimeMillis() - start));
		return null;

	}

	@Override
	public Void call() throws Exception {

		return parse();
	}

}
