package ie.gmit.sw;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;

public class Comparer {

	private FileParser fileParser;
	private BlockingQueue<Shingle> shingleQueue;

	public static void main(String[] args) {
		new Comparer().start();
	}

	public Comparer() {
		shingleQueue = new LinkedBlockingQueue<>();
		fileParser = new FileParser(Paths.get("res/test.txt"), shingleQueue, 4, 0);
	}

	private void start() {

		// fileParser.parse();

		// Thread t1 = new Thread(fileParser);
		//
		// t1.start();
		//
		// try {
		// t1.join();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		ExecutorService service = Executors.newFixedThreadPool(2);

		Future<List<Shingle>> listOne = service.submit(fileParser);

		fileParser = new FileParser(Paths.get("res/WarAndPeace.txt"), shingleQueue, 4, 1);

		Future<List<Shingle>> listTwo = service.submit(fileParser);

		service.shutdown();

		try {
			listOne.get().forEach(System.out::println);
			System.out.println(listOne.get().size());
			System.out.println(listTwo.get().size());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
