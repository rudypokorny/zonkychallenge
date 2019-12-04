package cz.rudypokorny.zonkychallenge.common;

/**
 * Service managing the periodic operation towards the {@link DataRequestor} and passing the obtained results to the {@link DataProcessor}.
 */
public interface Scheduler {

    /**
     * Executes the action
     */
    void executeScheduledAction();
}
