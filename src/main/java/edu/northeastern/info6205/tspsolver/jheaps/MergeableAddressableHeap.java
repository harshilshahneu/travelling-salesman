package edu.northeastern.info6205.tspsolver.jheaps;

import org.jheaps.AddressableHeap;

public interface MergeableAddressableHeap<K, V> extends AddressableHeap<K, V> {

	void meld(MergeableAddressableHeap<K, V> other);

}
