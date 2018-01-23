package io.hahahahaha.petiterpc.consumer;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.util.concurrent.AbstractFuture;

/**
 * 消费方Future.
 * <p>
 * 在set返回值或超时之前, 调用get线程将一直阻塞
 * </p>
 * {@link com.google.common.util.concurrent.SettableFuture}
 * 
 * @author shibinfei
 */
public final class ConsumerFuture<T> extends AbstractFuture<T> {

    public static ConsumerFuture<?> fromRequestId(long requestId) {
        return futureCache.get(requestId);
    }

    @SuppressWarnings("unused")
    private long requestId;

    private static ConcurrentMap<Long, ConsumerFuture<?>> futureCache = new ConcurrentHashMap<>();

    public ConsumerFuture(Method method, Object[] args, long requestId) {
        super();
        this.requestId = requestId;
        futureCache.put(requestId, this);
    }

    /**
     * 设置返回值
     * 
     * @param t
     */
    public void setReturnValue(T t) {
        super.set(t);
    }

    @SuppressWarnings("unchecked")
    public void setReturn(Object obj) {
        this.setReturnValue((T) obj);
    }

    /**
     * 设置返回异常
     * 
     * @param throwable
     */
    public void setReturnException(Throwable throwable) {
        super.setException(throwable);
    }

}
