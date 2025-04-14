package Progetto_Gruppo_33.visitors.execution;

public class InterpreterException extends RuntimeException {

	public InterpreterException() {
	}

	// Added a new InterpreterException to manage expected values
	public static InterpreterException ExpectedDynamicType(String type) {
		return new InterpreterException("Expected dynamic type " + type);
	}

	public InterpreterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InterpreterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InterpreterException(String message) {
		super(message);
	}

	public InterpreterException(Throwable cause) {
		super(cause);
	}
}
