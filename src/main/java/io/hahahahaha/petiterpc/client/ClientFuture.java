package io.hahahahaha.petiterpc.client;

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
public final class ClientFuture<T> extends AbstractFuture<T> {

    public static ClientFuture<?> fromRequestId(long requestId) {
        return futureCache.get(requestId);
    }

    @SuppressWarnings("unused")
    private long requestId;

    private static ConcurrentMap<Long, ClientFuture<?>> futureCache = new ConcurrentHashMap<>();

    public ClientFuture(Method method, Object[] args, long requestId) {
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
