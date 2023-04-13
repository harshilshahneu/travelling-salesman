package edu.northeastern.info6205.tspsolver.jheaps;

import java.util.Comparator;

public interface AddressableHeap<K, V> {

	interface Handle<K, V> {

		K getKey();

		V getValue();

		void setValue(V value);

		void decreaseKey(K newKey);

		void delete();

	}

	Comparator<? super K> comparator();

	Handle<K, V> insert(K key, V value);

	Handle<K, V> insert(K key);

	Handle<K, V> findMin();

	Handle<K, V> deleteMin();

	boolean isEmpty();

	long size();

	void clear();

}
