package io.hahahahaha.petiterpc.common;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 客户端发给服务端的请求
 * @author shibinfei
 *
 */
public class Request implements Codable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 843605030770777318L;

	private Class<?> interfaceClass;
	
	public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    private String methodName;
	
	private Object[] args;
	
	private long requestId;
	
	private static final AtomicLong REQUEST_ID_GENERATOR = new AtomicLong(0);
	
	public Request() {
		super();
		this.requestId = REQUEST_ID_GENERATOR.incrementAndGet();	// 溢出也没关系
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}


    @Override
	public String toString() {
		return "Request [interfaceClass=" + interfaceClass + ", methodName=" + methodName + ", args="
				+ Arrays.toString(args) + ", requestId=" + requestId + "]";
	}

	@Override
    public Type getType() {
        return Type.REQUEST;
    }
	
}
