package edu.northeastern.info6205.tspsolver.jheaps;

public interface MergeableAddressableHeap<K, V> extends AddressableHeap<K, V> {

	void meld(MergeableAddressableHeap<K, V> other);

}
