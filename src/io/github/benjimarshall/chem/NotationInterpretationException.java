package io.github.benjimarshall.chem;

/**
 * Notation Interpretation Exception. A {@code NotationInterpretationException} extends {@link Exception} and only
 * implements some new constructors
 * @author Benji Marshall
 * @since 2016-2-5
 * @see Exception
 */
public class NotationInterpretationException extends Exception {
    /**
     * Constructs an {@code NotationInterpretationException} object with no message or cause
     */
    public NotationInterpretationException() {
        super();
    }

    /**
     * Constructs an {@code NotationInterpretationException} object with a given message but no cause
     * @param message the message of the {@code NotationInterpretationException}
     */
    public NotationInterpretationException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code NotationInterpretationException} object with a given message, and cause
     * @param message the message of the {@code NotationInterpretationException}
     * @param cause the cause of the exception
     */
    public NotationInterpretationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@code NotationInterpretationException} object with a cause but no message
     * @param cause the cause of the exception
     */
    public NotationInterpretationException(Throwable cause) {
        super(cause);
    }
}
