package cz.rudypokorny.zonkychallenge.zonkyapi.service;

import cz.rudypokorny.zonkychallenge.common.DataProcessor;
import cz.rudypokorny.zonkychallenge.common.DataRetriever;
import cz.rudypokorny.zonkychallenge.configuration.ZonkyCustomProperties;
import cz.rudypokorny.zonkychallenge.zonkyapi.domain.MarketplaceLoan;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;

/**
 * Service managing the periodic operation towards the {@link DataRetriever} and passing the obtained results to the {@link DataProcessor}.
 */
@Log4j2
@Service
public class ZonkyApiSchedulerService {

    private static final String PARAM_LOANS_FIXED_DELAY = "${zonky.loans.fixedDelay}";

    private final DataRetriever<MarketplaceLoan> zonkyRestApiRetriever;
    private final DataProcessor<MarketplaceLoan> mongoProcessor;
    private final Clock clock;
    private final ZonkyCustomProperties customProperties;

    /**
     * Explicitly define the bean names to be able to define multiple similar schedulers
     *
     * @param dataRetriever    to retrieve data
     * @param dataProcessor    to process data
     * @param clock            representing the time service
     * @param customProperties access to application properties
     */
    @Autowired
    @SuppressWarnings("unchecked")
    public ZonkyApiSchedulerService(@Qualifier("zonkyLoansRestDataRetriever") DataRetriever dataRetriever,
                                    @Qualifier("zonkyLoansMongoDataProcessor") DataProcessor dataProcessor,
                                    Clock clock, ZonkyCustomProperties customProperties) {
        this.zonkyRestApiRetriever = dataRetriever;
        this.mongoProcessor = dataProcessor;
        this.clock = clock;
        this.customProperties = customProperties;
    }

    @Scheduled(fixedDelayString = PARAM_LOANS_FIXED_DELAY)
    public void executeScheduledAction() {
        log.info(String.format("Executing scheduled action by '%s' and passing the result to '%s'", zonkyRestApiRetriever.getName(), mongoProcessor.getName()));
        try {
            final List<MarketplaceLoan> loans = zonkyRestApiRetriever.retrieveData();
            if (loans.size() > 0) {
                log.debug(String.format("Processing %s items", loans.size()));

                loans.forEach(mongoProcessor::process);
            } else {
                log.warn("Execution does not returned any data");
            }
        } catch (Exception e) {
            log.error(String.format("Execution had failed because of %s", e.getMessage()), e);
        }

        log.info(String.format("Next execution will be made at: %s (in %d seconds)",
                clock.instant().plusMillis(customProperties.getFixedDelay()), customProperties.getFixedDelay() / 1000));
    }
}
