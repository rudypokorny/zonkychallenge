package cz.rudypokorny.zonkychallenge.common;


import java.util.List;

/**
 * Service interface used for data retrieving. Each implementation is bound to specific data object type {@link T}.
 * No need to use generics here ({@link ExternalData} would be enough), this is just to show off that I know the generics :-)
 *
 * @param <T> generic data object representation
 */
public interface DataRequestor<T extends ExternalData> {


    /**
     * Returns the {@link DataRequestor}'s name.
     *
     * @return name
     */
    String getName();

    /**
     * Fetches the data. The response is wrapped in {@link List}
     *
     * @return list of {@link T} representing the obtained data
     */
    List<T> requestData();

}
