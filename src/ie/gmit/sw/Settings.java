/* Class: Settings.java
 * Author: Bastian Graebener - G00340600 */
package ie.gmit.sw;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The Settings class stores all information needed to calculate a Jaccard Index.
 *
 * @author Bastian Graebener
 */
public class Settings {

	private int numOfFiles;
	private int numOfThreads;
	private int shingleSize;
	private int numOfHashes;
	private List<Path> fileLocations;

	/**
	 * Creates a new settings instance with default values for all fields.
	 * <p>
	 * Sets default values for the shingle sizes, the number of documents, the number of minHashes and the number of
	 * worker threads. Maintains a list of all file locations entered by the user.
	 */
	public Settings() {
		this.numOfFiles = 2;
		this.numOfThreads = 200;
		this.shingleSize = 4;
		this.numOfHashes = 200;
		this.fileLocations = new ArrayList<>(numOfFiles);
	}

	/**
	 * Gets the number of files.
	 *
	 * @return the number of files
	 */
	public int getNumOfFiles() {
		return numOfFiles;
	}

	/**
	 * Sets the number of files.
	 *
	 * @param numOfFiles
	 *            the new number of files
	 */
	public void setNumOfFiles(int numOfFiles) {
		this.numOfFiles = numOfFiles;
	}

	/**
	 * Gets the number of threads.
	 *
	 * @return the number of threads
	 */
	public int getNumOfThreads() {
		return numOfThreads;
	}

	/**
	 * Sets the number of threads.
	 *
	 * @param numOfThreads
	 *            the new number of threads
	 */
	public void setNumOfThreads(int numOfThreads) {
		this.numOfThreads = numOfThreads;
	}

	/**
	 * Gets the number of words a shingle is made up of.
	 *
	 * @return the shingle size
	 */
	public int getShingleSize() {
		return shingleSize;
	}

	/**
	 * Sets the number of words a shingle is made up of.
	 *
	 * @param shingleSize
	 *            the new shingle size
	 */
	public void setShingleSize(int shingleSize) {
		this.shingleSize = shingleSize;
	}

	/**
	 * Gets the number of hashes.
	 *
	 * @return the number of hashes
	 */
	public int getNumOfHashes() {
		return numOfHashes;
	}

	/**
	 * Sets the number of hashes.
	 *
	 * @param numOfHashes
	 *            the new number of hashes
	 */
	public void setNumOfHashes(int numOfHashes) {
		this.numOfHashes = numOfHashes;
	}

	/**
	 * Gets the the list of file locations.
	 *
	 * @return the list of file locations
	 */
	public List<Path> getFileLocations() {
		return fileLocations;
	}

	/**
	 * Gets the number of poison pills.
	 *
	 * @return the number of poison pills
	 */
	public int getNumOfPoisonPills() {
		return numOfThreads / numOfFiles + 1;
	}

	@Override
	public String toString() {
		return "Settings [numOfFiles=" + numOfFiles + ", numOfThreads=" + numOfThreads
				+ ", shingleSize=" + shingleSize + ", numOfHashes=" + numOfHashes + ", "
				+ (fileLocations != null ? "fileLocations=" + fileLocations + ", " : "") + "]";
	}

}
