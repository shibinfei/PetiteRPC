package io.hahahahaha.petiterpc.transport;

import java.util.concurrent.atomic.AtomicInteger;

import io.hahahahaha.petiterpc.common.Address;
import io.hahahahaha.petiterpc.common.ReadWriteList;

/**
 * @author shibinfei
 *
 */
public class AddressChannelList {

	private Address address;
	
	private ReadWriteList<TransportChannel> channels;

	private AtomicInteger index = new AtomicInteger(0);
	
	public AddressChannelList(Address address, ReadWriteList<TransportChannel> channels) {
		super();
		this.address = address;
		this.channels = channels;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public ReadWriteList<TransportChannel> getChannels() {
		return channels;
	}
	
	public TransportChannel next() {
	    int _index = index.getAndIncrement() & Integer.MAX_VALUE;
	    return channels.get(_index % channels.size());
	}
	
	public boolean isEmpty() {
	    return channels.isEmpty();
	}
	
	public void add(TransportChannel channel) {
	    this.channels.add(channel);
	}
	
	public void remove(TransportChannel channel) {
	    this.channels.remove(channel);
	}
	
	@Override
	public int hashCode() {
	    // TODO Auto-generated method stub
	    return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
	    return super.equals(obj);
	}

    @Override
    public String toString() {
        return "AddressChannelList [address=" + address + ", channels=" + channels + ", index=" + index + "]";
    }
	
}
