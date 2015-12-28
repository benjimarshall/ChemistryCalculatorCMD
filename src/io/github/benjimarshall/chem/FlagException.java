package io.github.benjimarshall.chem;

public class FlagException extends Exception {
    public FlagException() {
        super();
    }
    public FlagException(String message) {
        super(message);
    }
    public FlagException(String message, Throwable cause) {
        super(message, cause);
    }
    public FlagException(Throwable cause) {
        super(cause);
    }
}
