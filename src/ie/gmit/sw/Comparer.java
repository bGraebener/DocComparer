package ie.gmit.sw;

import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

public class Comparer {

	private Parser fileParser;
	private BlockingQueue<Shingle> shingleQueue;

	public static void main(String[] args) {
		new Comparer().start();
	}

	public Comparer() {
		fileParser = new FileParser(Paths.get("res/WarAndPeace.txt"), shingleQueue, 17, 0);
	}

	private void start() {

		fileParser.parse();

	}

}
