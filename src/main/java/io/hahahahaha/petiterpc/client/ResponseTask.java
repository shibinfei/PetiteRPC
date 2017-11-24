package io.hahahahaha.petiterpc.client;

import io.hahahahaha.petiterpc.common.Response;
import io.hahahahaha.petiterpc.transport.TransportChannel;

/**
 * @author shibinfei
 *
 */
public class ResponseTask implements Runnable {

	private final TransportChannel transportChannel;
	
	private final Response response;
	
	public ResponseTask(TransportChannel transportChannel, Response response) {
		this.transportChannel = transportChannel;
		this.response = response;
	}

	@Override
	public void run() {
		ClientFuture<?> clientFuture = ClientFuture.fromRequestId(response.getRequestId());
		
		if (response.getResult() instanceof Throwable) {
		    clientFuture.setReturnException((Throwable)response.getResult());
		} else {
		    clientFuture.setReturn(response.getResult());
		}
		
	}

}
