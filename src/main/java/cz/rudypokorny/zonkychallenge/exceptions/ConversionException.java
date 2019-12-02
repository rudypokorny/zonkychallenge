package cz.rudypokorny.zonkychallenge.exceptions;

public class ConversionException extends Exception {

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(Throwable cause) {
        super("Conversion has failed because of: " + cause.getMessage(), cause);
    }
}
