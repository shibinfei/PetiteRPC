package io.hahahahaha.petiterpc.common;

/**
 * Server返回结果封装
 * 
 * @author shibinfei
 *
 */
public class Response implements Codable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7191363762487262334L;

	private long requestId;

	private Object result;

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		return "Response [requestId=" + requestId + ", result=" + result + "]";
	}

    @Override
    public Type getType() {
        return Type.RESPONSE;
    }

}
