package cz.rudypokorny.zonkychallenge.zonkyapi.service;

import cz.rudypokorny.zonkychallenge.common.ApiUrlBuilder;
import cz.rudypokorny.zonkychallenge.common.DataRetriever;
import cz.rudypokorny.zonkychallenge.configuration.ZonkyCustomProperties;
import cz.rudypokorny.zonkychallenge.loan.service.LoanService;
import cz.rudypokorny.zonkychallenge.zonkyapi.domain.MarketplaceLoan;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Clock;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@Qualifier("zonkyLoansRestDataRetriever")
public class ZonkyRestApiDataRetriever implements DataRetriever<MarketplaceLoan> {

    public static final String RETRIEVER_NAME = "RestApi";
    public static final String PARAMETER_DATE_PUBLISHED__GT = "datePublished__gt";
    public static final String HEADER_X_SIZE = "X-Size";

    private final ZonkyCustomProperties customProperties;
    private final RestTemplate restTemplate;
    private final LoanService loanService;
    private final Clock clock;

    @Autowired
    public ZonkyRestApiDataRetriever(ZonkyCustomProperties customProperties,
                                     RestTemplate restTemplate, LoanService loanService, Clock clock) {
        this.customProperties = customProperties;
        this.restTemplate = restTemplate;
        this.loanService = loanService;
        this.clock = clock;
    }

    @Override
    public String getName() {
        return RETRIEVER_NAME;
    }

    @Override
    public List<MarketplaceLoan> retrieveData() {

        final ZonedDateTime searchFrom = loanService.findMostRecentlyPublished()
                .map(latestLoan -> latestLoan.getDatePublished())
                .orElse(ZonedDateTime.ofInstant(clock.instant(), ZoneOffset.UTC)
                        .minusHours(customProperties.getBackwardInterval()));

        final URI apiUrl = ApiUrlBuilder.create(customProperties.getUrl(), customProperties.getUrlParams())
                .addDateQueryParam(PARAMETER_DATE_PUBLISHED__GT, searchFrom)
                .buildToURI();

        log.debug(String.format("Retrieving data from: '%s'", apiUrl));

        //TODO move this to separate class
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_X_SIZE, customProperties.getPageSize());
        HttpEntity entity = new HttpEntity(headers);

        final ResponseEntity<MarketplaceLoan[]> result = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, MarketplaceLoan[].class);

        return result == null ? Collections.EMPTY_LIST : Arrays.asList(result.getBody());
    }

}
