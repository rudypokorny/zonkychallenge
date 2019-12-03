package cz.rudypokorny.zonkychallenge.loan.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZonkyChallengeStatus {

    private long totalLoansCount;
    private ZonedDateTime latestDatePublished;
    private int refreshInterval;
    private int defaultSearchRange;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> additionalAttributes;

}
