package io.hahahahaha.petiterpc.common.lambdatemplate;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * A template class to simplify {@link ReentrantReadWriteLock} operations based on lambda.
 * @author shibinfei
 *
 */
public class ReadWriteLockTemplate {

	private ReentrantReadWriteLock.ReadLock readLock;
	
	private ReentrantReadWriteLock.WriteLock writeLock;
	
	public ReadWriteLockTemplate() {
		super();
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		readLock = lock.readLock();
		writeLock = lock.writeLock();
	}

	/**
	 * Do in readLock
	 * @param supplier
	 * @return
	 */
	public <T> T read(Supplier<T> supplier) {
		readLock.lock();
		try {
			return supplier.get();
		} finally {
			readLock.unlock();
		}
	}
	
	
	/**
	 * Do in writeLock
	 * @param runnable
	 */
	public void write(Runnable runnable) {
		writeLock.lock();
		try {
			runnable.run();
		} finally {
			writeLock.unlock();
		}
	}
	
	
	public ReentrantReadWriteLock.ReadLock readLock() {
		return readLock;
	}
	
	public ReentrantReadWriteLock.WriteLock writeLock() {
		return writeLock;
	}
	
}
