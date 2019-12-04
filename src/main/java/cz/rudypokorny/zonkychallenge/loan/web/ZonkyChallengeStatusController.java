package cz.rudypokorny.zonkychallenge.loan.web;

import cz.rudypokorny.zonkychallenge.loan.domain.LoanInfo;
import cz.rudypokorny.zonkychallenge.loan.service.LoanService;
import cz.rudypokorny.zonkychallenge.loan.web.dto.ZonkyChallengeStatus;
import cz.rudypokorny.zonkychallenge.zonkyapi.configuration.ZonkyCustomProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides basic data about application state, its internals, etc.
 */
@RestController
public class ZonkyChallengeStatusController extends AbstractController {

    public static final String API_STATUS = API_BASE_URL + "/status";

    private final ZonkyCustomProperties customProperties;
    private final LoanService loanService;

    @Autowired
    public ZonkyChallengeStatusController(ZonkyCustomProperties customProperties, LoanService loanService) {
        this.customProperties = customProperties;
        this.loanService = loanService;
    }

    @GetMapping(API_STATUS)
    public ResponseEntity<ZonkyChallengeStatus> getStatus() {
        return ResponseEntity.ok(buildChallengeStatus());
    }

    private ZonkyChallengeStatus buildChallengeStatus() {
        return ZonkyChallengeStatus.builder()
                .latestDatePublished(loanService.findMostRecentlyPublished()
                        .map(LoanInfo::getDatePublished)
                        .orElse(null))
                .totalLoansCount(loanService.getTotaůLoansCount())
                .refreshInterval(customProperties.getRefreshInterval())
                .defaultSearchRange(customProperties.getDefaultSearchRange())
                .build();
    }
}
