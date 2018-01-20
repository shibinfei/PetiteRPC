package io.hahahahaha.petiterpc.transport;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;

import io.hahahahaha.petiterpc.common.Address;
import io.hahahahaha.petiterpc.common.ReadWriteList;

/**
 * @author shibinfei
 *
 */
public enum ChannelManager {
	
	INSTANCE;
	
	public static ChannelManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 每个地址对应一组Channel. (即一组Netty channel)
	 */
	ConcurrentMap<Address, AddressChannelList> addressChannelListMapping = Maps.newConcurrentMap();
	
	/**
	 * 服务对应的Channel地址
	 */
	ConcurrentMap<Class<?>, ReadWriteList<AddressChannelList>> serviceChannelListMapping = Maps.newConcurrentMap();
	
	/**
	 * 根据服务查找对应的所有的Channel组. 
	 * @param serviceClass
	 * @return
	 */
	public ReadWriteList<AddressChannelList> lookup(Class<?> serviceClass) {
	    return serviceChannelListMapping.computeIfAbsent(serviceClass, key -> new ReadWriteList<>());
	}
	
	
	public boolean isServiceAvaiable(Class<?> serviceClass) {
	    ReadWriteList<AddressChannelList> channelLists = lookup(serviceClass);
	    System.out.println(channelLists);
	    for (AddressChannelList l : channelLists) {
	        if (!l.isEmpty()) {
	            return true;
	        }
	    }
	    
	    return false;
	}
	
	
	/**
	 * 建立服务和Channel组的映射    
	 * @param serviceClass
	 * @param addressChannelList
	 * @return
	 */
	public void manage(Class<?> serviceClass, AddressChannelList addressChannelList) {
		ReadWriteList<AddressChannelList> list = lookup(serviceClass);
		list.add(addressChannelList);
	}
	
	
	public void remove(Class<?> serviceClass, AddressChannelList addressChannelList) {
	    ReadWriteList<AddressChannelList> list = lookup(serviceClass);
	    list.remove(addressChannelList);
	}
	
	/**
	 * 根据地址获取对应的Channel组
	 * @param address
	 * @return
	 */
	public AddressChannelList getByAddress(Address address) {
	    return addressChannelListMapping.computeIfAbsent(address, key -> new AddressChannelList(address, new ReadWriteList<>()));
	}
}