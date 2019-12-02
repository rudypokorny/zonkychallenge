package cz.rudypokorny.zonkychallenge.common;

/**
 * Service interface designed for data processing. Each implementation is bound to specific data object type {@link T}.
 * No need to use generics here ({@link ExternalData} would be enough), this is just to show off that I know the generics :-)
 *
 * @param <T> generic data object representation
 */
public interface DataProcessor<T extends ExternalData> {


    /**
     * {#link {@link DataProcessor}'s name.
     *
     * @return name
     */
    String getName();

    /**
     * Process the given data object of the type {@link ExternalData}.
     *
     * @param data be processed
     */
    void process(final T data);
}
