package cz.rudypokorny.zonkychallenge.zonkyapi.service;

import cz.rudypokorny.zonkychallenge.common.ApiUrlBuilder;
import cz.rudypokorny.zonkychallenge.common.DataRequestor;
import cz.rudypokorny.zonkychallenge.loan.service.LoanService;
import cz.rudypokorny.zonkychallenge.zonkyapi.configuration.ZonkyCustomProperties;
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
@Qualifier("zonkyLoansRestDataRequestorr")
public class ZonkyRestApiDataRequestor implements DataRequestor<MarketplaceLoan> {

    private static final String REQUESTOR_NAME = "RestApi";
    private static final String PARAMETER_DATE_PUBLISHED__GT = "datePublished__gt";
    private static final String HEADER_X_SIZE = "X-Size";

    private final ZonkyCustomProperties customProperties;
    private final RestTemplate restTemplate;
    private final LoanService loanService;
    private final Clock clock;

    @Autowired
    public ZonkyRestApiDataRequestor(ZonkyCustomProperties customProperties,
                                     RestTemplate restTemplate, LoanService loanService, Clock clock) {
        this.customProperties = customProperties;
        this.restTemplate = restTemplate;
        this.loanService = loanService;
        this.clock = clock;
    }

    @Override
    public String getName() {
        return REQUESTOR_NAME;
    }

    @Override
    public List<MarketplaceLoan> requestData() {

        final ZonedDateTime dateFrom = loanService.findMostRecentlyPublished()
                .map(latestLoan -> {
                    log.info(String.format("Latest published load was at: %s. Resuming from that time.", latestLoan.getDatePublished()));
                    return latestLoan.getDatePublished();
                })
                .orElseGet(() -> {
                    ZonedDateTime defaultTime = ZonedDateTime.ofInstant(clock.instant(), ZoneOffset.UTC).minusHours(customProperties.getDefaultSearchRange());
                    log.info(String.format("No loans found. Using default: %s", defaultTime));
                    return defaultTime;
                });

        final URI requestURI = constructURI(dateFrom);

        final ResponseEntity<MarketplaceLoan[]> result = restTemplate.exchange(requestURI, HttpMethod.GET, createHeaders(), MarketplaceLoan[].class);

        //TODO solve possible NPE
        return result == null ? Collections.emptyList() : Arrays.asList(result.getBody());
    }

    private URI constructURI(ZonedDateTime searchFrom) {
        URI uri = ApiUrlBuilder.create(customProperties.getUrl(), customProperties.getUrlParams())
                    .addDateQueryParam(PARAMETER_DATE_PUBLISHED__GT, searchFrom)
                    .buildToURI();
        log.debug(String.format("Constructing request: '%s'", uri));
        return uri;
    }

    private HttpEntity createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_X_SIZE, customProperties.getPageSize());
        return new HttpEntity(headers);
    }

}
