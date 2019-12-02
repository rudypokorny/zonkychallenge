package cz.rudypokorny.zonkychallenge.exceptions;


public class ConverterNotFoundException extends Exception {

    private final Object convertFrom;

    public ConverterNotFoundException(final Object convertFrom) {
        super(String.format("Converter not found for entity %s", convertFrom));
        this.convertFrom = convertFrom;
    }
}
