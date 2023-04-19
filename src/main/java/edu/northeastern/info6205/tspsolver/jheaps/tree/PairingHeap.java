package edu.northeastern.info6205.tspsolver.jheaps.tree;

import edu.northeastern.info6205.tspsolver.jheaps.MergeableAddressableHeap;

import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class PairingHeap<K, V> implements MergeableAddressableHeap<K, V>, Serializable {
	private static final long serialVersionUID = 1L;

	private final Comparator<? super K> comparator;

	private Node<K, V> root;

	private long size;

	private PairingHeap<K, V> other;

	public PairingHeap() {
		this(null);
	}

	public PairingHeap(Comparator<? super K> comparator) {
		this.root = null;
		this.comparator = comparator;
		this.size = 0;
		this.other = this;
	}

	@Override
	public Handle<K, V> insert(K key, V value) {
		if (other != this) {
			throw new IllegalStateException("A heap cannot be used after a meld");
		}
		if (key == null) {
			throw new NullPointerException("Null keys not permitted");
		}
		Node<K, V> n = new Node<K, V>(this, key, value);
		if (comparator == null) {
			root = link(root, n);
		} else {
			root = linkWithComparator(root, n);
		}
		size++;
		return n;
	}

	@Override
	public Handle<K, V> insert(K key) {
		return insert(key, null);
	}

	@Override
	public Handle<K, V> findMin() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		return root;
	}

	@Override
	public Handle<K, V> deleteMin() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		// assert root.o_s == null && root.y_s == null;

		Handle<K, V> oldRoot = root;

		// cut all children, combine them and overwrite old root
		root = combine(cutChildren(root));

		// decrease size
		size--;

		return oldRoot;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public long size() {
		return size;
	}

	@Override
	public Comparator<? super K> comparator() {
		return comparator;
	}

	@Override
	public void clear() {
		root = null;
		size = 0;
	}

	@Override
	public void meld(MergeableAddressableHeap<K, V> other) {
		PairingHeap<K, V> h = (PairingHeap<K, V>) other;

		// check same comparator
		if (comparator != null) {
			if (h.comparator == null || !h.comparator.equals(comparator)) {
				throw new IllegalArgumentException("Cannot meld heaps using different comparators!");
			}
		} else if (h.comparator != null) {
			throw new IllegalArgumentException("Cannot meld heaps using different comparators!");
		}

		if (h.other != h) {
			throw new IllegalStateException("A heap cannot be used after a meld.");
		}

		// perform the meld
		size += h.size;
		if (comparator == null) {
			root = link(root, h.root);
		} else {
			root = linkWithComparator(root, h.root);
		}

		// clear other
		h.size = 0;
		h.root = null;

		// take ownership
		h.other = this;
	}

	// --------------------------------------------------------------------
	static class Node<K, V> implements Handle<K, V>, Serializable {

		private final static long serialVersionUID = 1;

		PairingHeap<K, V> heap;

		K key;
		V value;
		Node<K, V> o_c; // older child
		Node<K, V> y_s; // younger sibling
		Node<K, V> o_s; // older sibling or parent

		Node(PairingHeap<K, V> heap, K key, V value) {
			this.heap = heap;
			this.key = key;
			this.value = value;
			this.o_c = null;
			this.y_s = null;
			this.o_s = null;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public void setValue(V value) {
			this.value = value;
		}

		@Override
		public void decreaseKey(K newKey) {
			getOwner().decreaseKey(this, newKey);
		}

		@Override
		public void delete() {
			getOwner().delete(this);
		}

		PairingHeap<K, V> getOwner() {
			if (heap.other != heap) {
				// find root
				PairingHeap<K, V> root = heap;
				while (root != root.other) {
					root = root.other;
				}
				// path-compression
				PairingHeap<K, V> cur = heap;
				while (cur.other != root) {
					PairingHeap<K, V> next = cur.other;
					cur.other = root;
					cur = next;
				}
				heap = root;
			}
			return heap;
		}
	}

	@SuppressWarnings("unchecked")
	private void decreaseKey(Node<K, V> n, K newKey) {
		int c;
		if (comparator == null) {
			c = ((Comparable<? super K>) newKey).compareTo(n.key);
		} else {
			c = comparator.compare(newKey, n.key);
		}

		if (c > 0) {
			throw new IllegalArgumentException("Keys can only be decreased!");
		}
		n.key = newKey;
		if (c == 0 || root == n) {
			return;
		}

		if (n.o_s == null) {
			throw new IllegalArgumentException("Invalid handle!");
		}

		// unlink from parent
		if (n.y_s != null) {
			n.y_s.o_s = n.o_s;
		}
		if (n.o_s.o_c == n) { // I am the oldest :(
			n.o_s.o_c = n.y_s;
		} else { // I have an older sibling!
			n.o_s.y_s = n.y_s;
		}
		n.y_s = null;
		n.o_s = null;

		// merge with root
		if (comparator == null) {
			root = link(root, n);
		} else {
			root = linkWithComparator(root, n);
		}
	}

	private void delete(Node<K, V> n) {
		if (root == n) {
			deleteMin();
			n.o_c = null;
			n.y_s = null;
			n.o_s = null;
			return;
		}

		if (n.o_s == null) {
			throw new IllegalArgumentException("Invalid handle!");
		}

		// unlink from parent
		if (n.y_s != null) {
			n.y_s.o_s = n.o_s;
		}
		if (n.o_s.o_c == n) { // I am the oldest :(
			n.o_s.o_c = n.y_s;
		} else { // I have an older sibling!
			n.o_s.y_s = n.y_s;
		}
		n.y_s = null;
		n.o_s = null;

		// perform delete-min at tree rooted at this
		Node<K, V> t = combine(cutChildren(n));

		// and merge with other cut tree
		if (comparator == null) {
			root = link(root, t);
		} else {
			root = linkWithComparator(root, t);
		}

		size--;
	}

	private Node<K, V> combine(Node<K, V> l) {
		if (l == null) {
			return null;
		}

		assert l.o_s == null;

		// left-right pass
		Node<K, V> pairs = null;
		Node<K, V> it = l, p_it;
		if (comparator == null) { // no comparator
			while (it != null) {
				p_it = it;
				it = it.y_s;

				if (it == null) {
					// append last node to pair list
					p_it.y_s = pairs;
					p_it.o_s = null;
					pairs = p_it;
				} else {
					Node<K, V> n_it = it.y_s;

					// disconnect both
					p_it.y_s = null;
					p_it.o_s = null;
					it.y_s = null;
					it.o_s = null;

					// link trees
					p_it = link(p_it, it);

					// append to pair list
					p_it.y_s = pairs;
					pairs = p_it;

					// advance
					it = n_it;
				}
			}
		} else {
			while (it != null) {
				p_it = it;
				it = it.y_s;

				if (it == null) {
					// append last node to pair list
					p_it.y_s = pairs;
					p_it.o_s = null;
					pairs = p_it;
				} else {
					Node<K, V> n_it = it.y_s;

					// disconnect both
					p_it.y_s = null;
					p_it.o_s = null;
					it.y_s = null;
					it.o_s = null;

					// link trees
					p_it = linkWithComparator(p_it, it);

					// append to pair list
					p_it.y_s = pairs;
					pairs = p_it;

					// advance
					it = n_it;
				}
			}
		}

		// second pass (reverse order - due to add first)
		it = pairs;
		Node<K, V> f = null;
		if (comparator == null) {
			while (it != null) {
				Node<K, V> nextIt = it.y_s;
				it.y_s = null;
				f = link(f, it);
				it = nextIt;
			}
		} else {
			while (it != null) {
				Node<K, V> nextIt = it.y_s;
				it.y_s = null;
				f = linkWithComparator(f, it);
				it = nextIt;
			}
		}

		return f;
	}

	private Node<K, V> cutChildren(Node<K, V> n) {
		Node<K, V> child = n.o_c;
		n.o_c = null;
		if (child != null) {
			child.o_s = null;
		}
		return child;
	}

	@SuppressWarnings("unchecked")
	private Node<K, V> link(Node<K, V> f, Node<K, V> s) {
		if (s == null) {
			return f;
		} else if (f == null) {
			return s;
		} else if (((Comparable<? super K>) f.key).compareTo(s.key) <= 0) {
			s.y_s = f.o_c;
			s.o_s = f;
			if (f.o_c != null) {
				f.o_c.o_s = s;
			}
			f.o_c = s;
			return f;
		} else {
			return link(s, f);
		}
	}

	private Node<K, V> linkWithComparator(Node<K, V> f, Node<K, V> s) {
		if (s == null) {
			return f;
		} else if (f == null) {
			return s;
		} else if (comparator.compare(f.key, s.key) <= 0) {
			s.y_s = f.o_c;
			s.o_s = f;
			if (f.o_c != null) {
				f.o_c.o_s = s;
			}
			f.o_c = s;
			return f;
		} else {
			return linkWithComparator(s, f);
		}
	}

}
