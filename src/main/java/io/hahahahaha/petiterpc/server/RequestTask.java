package io.hahahahaha.petiterpc.server;

import io.hahahahaha.petiterpc.common.Request;
import io.hahahahaha.petiterpc.transport.TransportChannel;

/**
 * @author shibinfei
 *
 */
public class RequestTask implements Runnable {

    private TransportChannel transportChannel;
    
    private Request request;
    
    public RequestTask(TransportChannel transportChannel, Request request) {
        super();
        this.transportChannel = transportChannel;
        this.request = request;
    }

    public TransportChannel getTransportChannel() {
        return transportChannel;
    }

    public void setTransportChannel(TransportChannel transportChannel) {
        this.transportChannel = transportChannel;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public void run() {
        
        
        
    }
    
}
