package ie.gmit.sw;

/**
 *
 * @author Basti
 */
public class Shingle {

	private int docId;
	private int hash;

	public Shingle(int docId, int hash) {
		this.docId = docId;
		this.hash = hash;
	}

	protected Shingle() {

	}

	public int getDocId() {
		return docId;
	}

	public int getHash() {
		return hash;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hash;
		return result;
	}

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

	@Override
	public String toString() {
		return "Shingle [docId=" + docId + ", hash=" + hash + "]";
	}

}
