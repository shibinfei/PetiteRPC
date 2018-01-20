package io.hahahahaha.petiterpc.common;
/**
 * 
 * @author sbf
 *
 */

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Preconditions;

import io.hahahahaha.petiterpc.common.lambdatemplate.ReadWriteLockTemplate;

/**
 * A copy-on-write container base on {@link ReentrantReadWriteLock} The
 * difference from {@link CopyOnWriteArrayList} is whether do locking before
 * getting array snapshot.
 * 
 * @author shibinfei
 *
 */
public class ReadWriteList<T> implements Iterable<T> {

	private ReadWriteLockTemplate readWriteLockTemplate;

	private Object[] elements;

	public ReadWriteList() {
		elements = new Object[] {};
		readWriteLockTemplate = new ReadWriteLockTemplate();
	}

	/**
	 * 添加元素
	 * @param t
	 */
	public void add(T t) {
		readWriteLockTemplate.write(() -> {
			elements = Arrays.copyOf(elements, elements.length + 1);
			elements[elements.length - 1] = t;
		});
	}

	/**
	 * 大小
	 * @return
	 */
	public int size() {
		return readWriteLockTemplate.read(() -> elements.length);
	}

	public boolean isEmpty() {
		return size() == 0;
	}
	
	
	@SuppressWarnings("unchecked")
	public T get(int index) {
		return readWriteLockTemplate.read(() -> (T) elements[index]);
	}

	public void remove(Object o) {
		Preconditions.checkNotNull(o);

		readWriteLockTemplate.writeLock().lock();

		if (elements.length == 0) {
			return;
		}

		try {
			// Copy while searching for element to remove
			// This wins in the normal case of element being present
			int newlen = elements.length - 1;
			Object[] newElements = new Object[newlen];

			for (int i = 0; i < newlen; ++i) {
				if (Objects.equal(o, elements[i])) {
					// found one; copy remaining and exit
					for (int k = i + 1; k < elements.length; ++k)
						newElements[k - 1] = elements[k];
					elements = newElements;
					return;
				} else
					newElements[i] = elements[i];
			}

			// special handling for last cell
			if (Objects.equal(o, elements[newlen])) {
				elements = newElements;
			}
		} finally {
			readWriteLockTemplate.writeLock().unlock();
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new ReadWriteItr();
	}

	class ReadWriteItr implements Iterator<T> {

		private Object[] snapshot;

		private int cursor;

		public ReadWriteItr() {
			snapshot = readWriteLockTemplate.read(() -> elements);
		}

		@Override
		public boolean hasNext() {
			return cursor < snapshot.length;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return (T) snapshot[cursor++];
		}

	}
	
	@Override
	public String toString() {
		ToStringHelper toStringHelper = Objects.toStringHelper("ReadWriteList");
		for (T t : this) {
			toStringHelper.addValue(t);
		}
		return toStringHelper.toString();
	}
	
}
