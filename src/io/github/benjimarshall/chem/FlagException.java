package io.github.benjimarshall.chem;

/**
 * Flag Exception. A {@code FlagException} extends {@link Exception} and only implements some new constructors
 * @author Benji Marshall
 * @since 2016-2-5
 * @see Exception
 */
public class FlagException extends Exception {
    /**
     * Constructs an {@code FlagException} object with no message or cause
     */
    public FlagException() {
        super();
    }

    /**
     * Constructs an {@code FlagException} object with a given message but no cause
     * @param message the message of the {@code FlagException}
     */
    public FlagException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code FlagException} object with a given message, and cause
     * @param message the message of the {@code FlagException}
     * @param cause the cause of the exception
     */
    public FlagException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@code FlagException} object with a cause but no message
     * @param cause the cause of the exception
     */
    public FlagException(Throwable cause) {
        super(cause);
    }
}
