package edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5;

public class BlossomVOptions {

	public static final BlossomVOptions[] ALL_OPTIONS = new BlossomVOptions[] {
			new BlossomVOptions(InitializationType.NONE, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, true, true), // [0]
			new BlossomVOptions(InitializationType.NONE, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, true, false), // [1]
			new BlossomVOptions(InitializationType.NONE, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, false, true), // [2]
			new BlossomVOptions(InitializationType.NONE, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, false, false), // [3]
			new BlossomVOptions(InitializationType.NONE, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, true, true), // [4]
			new BlossomVOptions(InitializationType.NONE, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, true, false), // [5]
			new BlossomVOptions(InitializationType.NONE, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, false, true), // [6]
			new BlossomVOptions(InitializationType.NONE, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, false, false), // [7]
			new BlossomVOptions(InitializationType.GREEDY, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, true, true), // [8]
			new BlossomVOptions(InitializationType.GREEDY, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, true, false), // [9]
			new BlossomVOptions(InitializationType.GREEDY, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, false, true), // [10]
			new BlossomVOptions(InitializationType.GREEDY, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, false, false), // [11]
			new BlossomVOptions(InitializationType.GREEDY, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, true, true), // [12]
			new BlossomVOptions(InitializationType.GREEDY, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, true, false), // [13]
			new BlossomVOptions(InitializationType.GREEDY, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, false, true), // [14]
			new BlossomVOptions(InitializationType.GREEDY, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, false, true), // [15]
			new BlossomVOptions(InitializationType.FRACTIONAL, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, true, true), // [16]
			new BlossomVOptions(InitializationType.FRACTIONAL, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, true, false), // [17]
			new BlossomVOptions(InitializationType.FRACTIONAL, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, false, true), // [18]
			new BlossomVOptions(InitializationType.FRACTIONAL, DualUpdateStrategy.MULTIPLE_TREE_CONNECTED_COMPONENTS, false, false), // [19]
			new BlossomVOptions(InitializationType.FRACTIONAL, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, true, true), // [20]
			new BlossomVOptions(InitializationType.FRACTIONAL, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, true, false), // [21]
			new BlossomVOptions(InitializationType.FRACTIONAL, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, false, true), // [22]
			new BlossomVOptions(InitializationType.FRACTIONAL, DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA, false, true), // [23]
	};

	private static final InitializationType DEFAULT_INITIALIZATION_TYPE = InitializationType.FRACTIONAL;

	private static final DualUpdateStrategy DEFAULT_DUAL_UPDATE_TYPE = DualUpdateStrategy.MULTIPLE_TREE_FIXED_DELTA;

	private static final boolean DEFAULT_UPDATE_DUALS_BEFORE = true;

	private static final boolean DEFAULT_UPDATE_DUALS_AFTER = false;

	DualUpdateStrategy dualUpdateStrategy;

	InitializationType initializationType;

	boolean updateDualsBefore;

	boolean updateDualsAfter;

	public BlossomVOptions(InitializationType initializationType, DualUpdateStrategy dualUpdateStrategy,
			boolean updateDualsBefore, boolean updateDualsAfter) {
		this.dualUpdateStrategy = dualUpdateStrategy;
		this.initializationType = initializationType;
		this.updateDualsBefore = updateDualsBefore;
		this.updateDualsAfter = updateDualsAfter;
	}

	public BlossomVOptions(InitializationType initializationType) {
		this(initializationType, DEFAULT_DUAL_UPDATE_TYPE, DEFAULT_UPDATE_DUALS_BEFORE, DEFAULT_UPDATE_DUALS_AFTER);
	}

	public BlossomVOptions() {
		this(DEFAULT_INITIALIZATION_TYPE, DEFAULT_DUAL_UPDATE_TYPE, DEFAULT_UPDATE_DUALS_BEFORE,
				DEFAULT_UPDATE_DUALS_AFTER);
	}

	@Override
	public String toString() {
		return "BlossomVOptions{initializationType=" + initializationType + ", dualUpdateStrategy=" + dualUpdateStrategy
				+ ", updateDualsBefore=" + updateDualsBefore + ", updateDualsAfter=" + updateDualsAfter + '}';
	}

	public boolean isUpdateDualsBefore() {
		return updateDualsBefore;
	}

	public boolean isUpdateDualsAfter() {
		return updateDualsAfter;
	}

	public DualUpdateStrategy getDualUpdateStrategy() {
		return dualUpdateStrategy;
	}

	public InitializationType getInitializationType() {
		return initializationType;
	}

	public enum DualUpdateStrategy {
		MULTIPLE_TREE_FIXED_DELTA {
			@Override
			public String toString() {
				return "Multiple tree fixed delta";
			}
		},
		MULTIPLE_TREE_CONNECTED_COMPONENTS {
			@Override
			public String toString() {
				return "Multiple tree connected components";
			}
		};

		@Override
		public abstract String toString();
	}

	public enum InitializationType {
		GREEDY {
			@Override
			public String toString() {
				return "Greedy initialization";
			}
		},
		NONE {
			@Override
			public String toString() {
				return "None";
			}
		},
		FRACTIONAL {
			@Override
			public String toString() {
				return "Fractional matching initializations";
			}
		};

		@Override
		public abstract String toString();
	}
}
