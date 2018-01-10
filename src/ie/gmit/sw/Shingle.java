/* Class: Shingle.java
 * Author: Bastian Graebener - G00340600 */
package ie.gmit.sw;

/**
 * Shingles are numeric representations of a <code>String</code> of an arbitrary number of words or characters. The
 * numeric representation is stored as an integer.
 * Every <code>Shingle</code> belongs to a specific document indicated by a document id.
 *
 * @author Bastian Graebener
 */
public class Shingle {

	private int docId;
	private int hash;

	/**
	 * Constructs a new <code>Shingle</code> with a document id and the hash code that represents the
	 * <code>String</code>.
	 *
	 * @param docId
	 *            the document id the Shingle belongs to
	 * @param hash
	 *            the hash code of the <code>String</code>
	 */
	public Shingle(int docId, int hash) {
		this.docId = docId;
		this.hash = hash;
	}

	/**
	 * Gets the doc id.
	 *
	 * @return the document id the <code>Shingle</code> belongs to.
	 */
	public int getDocId() {
		return docId;
	}

	/**
	 * Gets the hash.
	 *
	 * @return the hash code that represents the <code>String</code>.
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
