package edu.northeastern.info6205.tspsolver.jgrapht.util;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.UUID;
import java.util.function.Supplier;

import edu.northeastern.info6205.tspsolver.jgrapht.graph.DefaultEdge;
import edu.northeastern.info6205.tspsolver.jgrapht.graph.DefaultWeightedEdge;

public class SupplierUtil {

	@SuppressWarnings("unchecked")
	public static final Supplier<DefaultEdge> DEFAULT_EDGE_SUPPLIER = (Supplier<DefaultEdge> & Serializable) DefaultEdge::new;

	@SuppressWarnings("unchecked")
	public static final Supplier<DefaultWeightedEdge> DEFAULT_WEIGHTED_EDGE_SUPPLIER = (Supplier<DefaultWeightedEdge> & Serializable) DefaultWeightedEdge::new;

	@SuppressWarnings("unchecked")
	public static final Supplier<Object> OBJECT_SUPPLIER = (Supplier<Object> & Serializable) Object::new;

	@SuppressWarnings("unchecked")
	public static <T> Supplier<T> createSupplier(Class<? extends T> clazz) {
		// shortcut to use pre-defined constructor method reference based suppliers
		if (clazz == DefaultEdge.class) {
			return (Supplier<T>) DEFAULT_EDGE_SUPPLIER;
		} else if (clazz == DefaultWeightedEdge.class) {
			return (Supplier<T>) DEFAULT_WEIGHTED_EDGE_SUPPLIER;
		} else if (clazz == Object.class) {
			return (Supplier<T>) OBJECT_SUPPLIER;
		}

		try {
			final Constructor<? extends T> constructor = clazz.getDeclaredConstructor();
			if ((!Modifier.isPublic(constructor.getModifiers())
					|| !Modifier.isPublic(constructor.getDeclaringClass().getModifiers()))

			) {
				constructor.setAccessible(true);
			}
			return new ConstructorSupplier<>(constructor);
		} catch (ReflectiveOperationException e) {
			// Defer throwing an exception to the first time the supplier is called
			return getThrowingSupplier(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> Supplier<T> getThrowingSupplier(Throwable e) {
		return (Supplier<T> & Serializable) () -> {
			throw new SupplierException(e.getMessage(), e);
		};
	}

	public static Supplier<DefaultEdge> createDefaultEdgeSupplier() {
		return DEFAULT_EDGE_SUPPLIER;
	}

	public static Supplier<DefaultWeightedEdge> createDefaultWeightedEdgeSupplier() {
		return DEFAULT_WEIGHTED_EDGE_SUPPLIER;
	}

	public static Supplier<Integer> createIntegerSupplier() {
		return createIntegerSupplier(0);
	}

	@SuppressWarnings("unchecked")
	public static Supplier<Integer> createIntegerSupplier(int start) {
		int[] modifiableInt = new int[] { start }; // like a modifiable int
		return (Supplier<Integer> & Serializable) () -> modifiableInt[0]++;
	}

	public static Supplier<Long> createLongSupplier() {
		return createLongSupplier(0);
	}

	@SuppressWarnings("unchecked")
	public static Supplier<Long> createLongSupplier(long start) {
		long[] modifiableLong = new long[] { start }; // like a modifiable long
		return (Supplier<Long> & Serializable) () -> modifiableLong[0]++;
	}

	public static Supplier<String> createStringSupplier() {
		return createStringSupplier(0);
	}

	@SuppressWarnings("unchecked")
	public static Supplier<String> createRandomUUIDStringSupplier() {
		return (Supplier<String> & Serializable) () -> UUID.randomUUID().toString();
	}

	@SuppressWarnings("unchecked")
	public static Supplier<String> createStringSupplier(int start) {
		int[] container = new int[] { start };
		return (Supplier<String> & Serializable) () -> String.valueOf(container[0]++);
	}

	private static class ConstructorSupplier<T> implements Supplier<T>, Serializable {
		private final Constructor<? extends T> constructor;

		private static class SerializedForm<T> implements Serializable {
			private static final long serialVersionUID = -2385289829144892760L;

			private final Class<T> type;

			public SerializedForm(Class<T> type) {
				this.type = type;
			}

			Object readResolve() throws ObjectStreamException {
				try {
					return new ConstructorSupplier<>(type.getDeclaredConstructor());
				} catch (ReflectiveOperationException e) {
					InvalidObjectException ex = new InvalidObjectException(
							"Failed to get no-args constructor from " + type);
					ex.initCause(e);
					throw ex;
				}
			}
		}

		public ConstructorSupplier(Constructor<? extends T> constructor) {
			this.constructor = constructor;
		}

		@Override
		public T get() {
			try {
				return constructor.newInstance();
			} catch (ReflectiveOperationException ex) {
				throw new SupplierException("Supplier failed", ex);
			}
		}

		Object writeReplace() throws ObjectStreamException {
			return new SerializedForm<>(constructor.getDeclaringClass());
		}
	}
}
