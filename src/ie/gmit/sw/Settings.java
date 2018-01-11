/* Class: Settings.java
 * Author: Bastian Graebener - G00340600 */
package ie.gmit.sw;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Settings.
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
	 * Instantiates a new settings.
	 *
	 * <p>
	 *
	 * </p>
	 */
	public Settings() {
		this(2, 4, 200, 200);
	}

	/**
	 * Instantiates a new settings.
	 *
	 * <p>
	 *
	 * </p>
	 *
	 * @param numOfFiles
	 *            the num of files
	 * @param shingleSize
	 *            the shingle size
	 * @param numOfThreads
	 *            the num of threads
	 * @param numOfHashes
	 *            the num of hashes
	 */
	public Settings(int numOfFiles, int shingleSize, int numOfThreads, int numOfHashes) {
		this.numOfFiles = numOfFiles;
		this.numOfThreads = numOfThreads;
		this.shingleSize = shingleSize;
		this.numOfHashes = numOfHashes;
		this.fileLocations = new ArrayList<>(numOfFiles);
	}

	/**
	 * Gets the num of files.
	 *
	 * @return the num of files
	 */
	public int getNumOfFiles() {
		return numOfFiles;
	}

	/**
	 * Sets the num of files.
	 *
	 * @param numOfFiles
	 *            the new num of files
	 */
	public void setNumOfFiles(int numOfFiles) {
		this.numOfFiles = numOfFiles;
	}

	/**
	 * Gets the num of threads.
	 *
	 * @return the num of threads
	 */
	public int getNumOfThreads() {
		return numOfThreads;
	}

	/**
	 * Sets the num of threads.
	 *
	 * @param numOfThreads
	 *            the new num of threads
	 */
	public void setNumOfThreads(int numOfThreads) {
		this.numOfThreads = numOfThreads;
	}

	/**
	 * Gets the shingle size.
	 *
	 * @return the shingle size
	 */
	public int getShingleSize() {
		return shingleSize;
	}

	/**
	 * Sets the shingle size.
	 *
	 * @param shingleSize
	 *            the new shingle size
	 */
	public void setShingleSize(int shingleSize) {
		this.shingleSize = shingleSize;
	}

	/**
	 * Gets the num of hashes.
	 *
	 * @return the num of hashes
	 */
	public int getNumOfHashes() {
		return numOfHashes;
	}

	/**
	 * Sets the num of hashes.
	 *
	 * @param numOfHashes
	 *            the new num of hashes
	 */
	public void setNumOfHashes(int numOfHashes) {
		this.numOfHashes = numOfHashes;
	}

	/**
	 * Gets the file locations.
	 *
	 * @return the file locations
	 */
	public List<Path> getFileLocations() {
		return fileLocations;
	}

	/**
	 * Sets the file locations.
	 *
	 * @param fileLocations
	 *            the new file locations
	 */
	public void setFileLocations(List<Path> fileLocations) {
		this.fileLocations = fileLocations;
	}

	/**
	 * Gets the num of poison pills.
	 *
	 * @return the num of poison pills
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
