package io.hahahahaha.petiterpc.loadbalancer;

import java.util.concurrent.atomic.AtomicInteger;

import io.hahahahaha.petiterpc.common.ReadWriteList;
import io.hahahahaha.petiterpc.transport.AddressChannelList;

/**
 * @author shibinfei
 *
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private AtomicInteger counter = new AtomicInteger(0);
    
    @Override
    public AddressChannelList select(ReadWriteList<AddressChannelList> addressChannelLists) {
        
        if (addressChannelLists == null || addressChannelLists.isEmpty()) {
            return null;
        }
        
        int index = counter.getAndIncrement() & Integer.MAX_VALUE;  // 保证index为非负  
        return addressChannelLists.get(index % addressChannelLists.size());
    }

}
