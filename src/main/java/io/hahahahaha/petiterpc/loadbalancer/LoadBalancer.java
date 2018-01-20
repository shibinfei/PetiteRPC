package io.hahahahaha.petiterpc.loadbalancer;

import io.hahahahaha.petiterpc.common.ReadWriteList;
import io.hahahahaha.petiterpc.transport.AddressChannelList;

/**
 * @author shibinfei
 *
 */
public interface LoadBalancer {

    /**
     * 从不同地址的ChannelList中选择出来一个. 
     * @param addressChannelLists
     * @return
     */
    AddressChannelList select(ReadWriteList<AddressChannelList> addressChannelLists);
    
}
