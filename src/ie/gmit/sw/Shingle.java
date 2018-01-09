/* Class: Shingle.java
 * Author: Bastian Graebener - G00340600 */
package ie.gmit.sw;

// TODO: Auto-generated Javadoc

/**
 * The Class Shingle.
 *
 * @author Bastian Graebener
 */
public class Shingle {

	private int docId;
	private int hash;

	/**
	 * Instantiates a new shingle.
	 *
	 * <p>
	 *
	 * </p>
	 *
	 * @param docId
	 *            the doc id
	 * @param hash
	 *            the hash
	 */
	public Shingle(int docId, int hash) {
		this.docId = docId;
		this.hash = hash;
	}

	// protected Shingle() {
	//
	// }

	/**
	 * Gets the doc id.
	 *
	 * @return the doc id
	 */
	public int getDocId() {
		return docId;
	}

	/**
	 * Gets the hash.
	 *
	 * @return the hash
	 */
	public int getHash() {
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hash;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Shingle other = (Shingle) obj;
		if (hash != other.hash) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString() */
	@Override
	public String toString() {
		return "Shingle [docId=" + docId + ", hash=" + hash + "]";
	}

}
