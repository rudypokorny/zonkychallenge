package cz.rudypokorny.zonkychallenge.zonkyapi.service;

import cz.rudypokorny.zonkychallenge.common.DataProcessor;
import cz.rudypokorny.zonkychallenge.common.DataRequestor;
import cz.rudypokorny.zonkychallenge.zonkyapi.configuration.ZonkyCustomProperties;
import cz.rudypokorny.zonkychallenge.zonkyapi.domain.MarketplaceLoan;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;

/**
 * Service managing the periodic operation towards the {@link DataRequestor} and passing the obtained results to the {@link DataProcessor}.
 */
@Log4j2
@Service
public class ZonkyApiScheduler {

    private static final String PARAM_LOANS_FIXED_DELAY = "${zonky.loans.fixedDelay}";

    private final DataRequestor<MarketplaceLoan> zonkyRestApiRequestor;
    private final DataProcessor<MarketplaceLoan> mongoProcessor;
    private final Clock clock;
    private final ZonkyCustomProperties customProperties;

    /**
     * Explicitly define the bean names to be able to define multiple similar schedulers
     *
     * @param dataRequestor    to retrieve data
     * @param dataProcessor    to process data
     * @param clock            representing the time service
     * @param customProperties access to application properties
     */
    @Autowired
    @SuppressWarnings("unchecked")
    public ZonkyApiScheduler(@Qualifier("zonkyLoansRestDataRequestorr") DataRequestor dataRequestor,
                             @Qualifier("zonkyLoansMongoDataProcessor") DataProcessor dataProcessor,
                             Clock clock, ZonkyCustomProperties customProperties) {
        this.zonkyRestApiRequestor = dataRequestor;
        this.mongoProcessor = dataProcessor;
        this.clock = clock;
        this.customProperties = customProperties;
    }

    @Scheduled(fixedDelayString = PARAM_LOANS_FIXED_DELAY)
    public void executeScheduledAction() {
        log.info(String.format("Executing scheduled action: '%s' -> '%s'", zonkyRestApiRequestor.getName(), mongoProcessor.getName()));
        try {
            final List<MarketplaceLoan> loans = zonkyRestApiRequestor.requestData();
            log.debug(String.format("Request returned %s items", loans.size()));
            if (loans.size() > 0) {
                loans.forEach(mongoProcessor::process);
            }
        } catch (Exception e) {
            log.error(String.format("Request had failed: %s", e.getMessage()), e);
        }

        log.info(String.format("Next request will be made at: %s (in %d seconds)",
                clock.instant().plusMillis(customProperties.getRefreshInterval()), customProperties.getRefreshInterval() / 1000));
    }
}
