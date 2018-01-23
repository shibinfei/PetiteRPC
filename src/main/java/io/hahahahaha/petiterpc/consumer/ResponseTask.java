package io.hahahahaha.petiterpc.consumer;

import io.hahahahaha.petiterpc.common.Response;

/**
 * @author shibinfei
 *
 */
public class ResponseTask implements Runnable {

	private final Response response;
	
	public ResponseTask(Response response) {
		this.response = response;
	}

	@Override
	public void run() {
		ConsumerFuture<?> clientFuture = ConsumerFuture.fromRequestId(response.getRequestId());
		
		if (response.getResult() instanceof Throwable) {
		    clientFuture.setReturnException((Throwable)response.getResult());
		} else {
		    clientFuture.setReturn(response.getResult());
		}
		
	}

}
