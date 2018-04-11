package io.hahahahaha.petiterpc.common;

public class Heartbeat implements Codable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5214033697225027327L;
	
	private boolean request;

	public boolean isRequest() {
		return request;
	}

	public void setRequest(boolean request) {
		this.request = request;
	}
	
	@Override
	public Type getType() {
		return Type.HEARTBEAT;
	}
	
}
