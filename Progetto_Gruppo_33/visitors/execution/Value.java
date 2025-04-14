package Progetto_Gruppo_33.visitors.execution;

public interface Value {
	/* default conversion methods */
	default int toInt() {
		throw new InterpreterException("Expecting an integer");
	}

	default boolean toBool() {
		throw new InterpreterException("Expecting a boolean");
	}

	default PairValue toPair() {
		throw new InterpreterException("Expecting a pair");
	}

	// Added a new toVect() method to the interface
	default int[] toVect(){
		throw new InterpreterException("Expecting a vector");
	}
}
