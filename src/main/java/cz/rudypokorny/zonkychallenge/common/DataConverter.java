package cz.rudypokorny.zonkychallenge.common;

import cz.rudypokorny.zonkychallenge.exceptions.ConversionException;

/**
 * Simple interface defines an API for bean conversion
 *
 * @param <T> generic type - output of the conversion
 * @param <S> generic type - input bean to be converted
 */
public interface DataConverter<T, S extends ExternalData> {

    /**
     * Transforms the given object to different one.
     *
     * @param input object to be converted
     * @return transformed object after conversion
     * @throws {@link ConversionException} if the conversion failed
     */
    T convert(S input) throws ConversionException;

    /**
     * Checks whether current Converter could convert the given object
     *
     * @param from object which is tested whether is convertible by this converter
     * @return TRUE if the current converter could convert the given object, FALSE otherwise
     */
    boolean supportsConversion(final Object from);
}
