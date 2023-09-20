package io.mongonq;

public class WriteResult {
	private int n;
	private boolean acknowledged;
	private boolean updateOfExisting;
	private Object upsertedId;

	public WriteResult() {
		n = 0;
		acknowledged = false;
		upsertedId = null;
	}
	
	public WriteResult(int n, boolean acknowledged, Object upsertedId) {
		this.n = n;
		this.acknowledged = acknowledged;
		this.upsertedId = upsertedId;
	}

	public WriteResult(int n, boolean acknowledged) {
		this.n = n;
		this.acknowledged = acknowledged;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public boolean wasAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	public boolean isUpdateOfExisting() {
		return updateOfExisting;
	}

	public void setUpdateOfExisting(boolean updateOfExisting) {
		this.updateOfExisting = updateOfExisting;
	}

	public Object getUpsertedId() {
		return upsertedId;
	}

	public void setUpsertedId(Object upsertedId) {
		this.upsertedId = upsertedId;
	}
}
