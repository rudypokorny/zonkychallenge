package cz.rudypokorny.zonkychallenge.loan.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.rudypokorny.zonkychallenge.zonkyapi.domain.MarketplaceLoan;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Special entity for persisting the loan in DB. It is almost the same as {@link MarketplaceLoan} entity,
 * but to promote loose coupling with Zonky API, its better to have as separate entity.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "loans")
public class LoanInfo {

    @Id
    private String id;
    @CreatedDate
    private LocalDateTime created;
    @LastModifiedDate
    private LocalDateTime modified;

    //the same values as in ZonkyLoan
    @NotNull
    private String loanId;
    private String url;
    private String name;
    private String story;
    private String purpose;
    private String nickName;
    private Integer termInMonths;
    private BigDecimal interestRate;
    private BigDecimal revenueRate;
    private Integer annuityWithInsurance;
    private String rating;
    private Boolean topped;
    private BigDecimal amount;
    private String countryOfOrigin;
    private String currency;
    private BigDecimal remainingInvestment;
    private BigDecimal reservedAmount;
    private BigDecimal investmentRate;
    private Boolean covered;
    private ZonedDateTime datePublished;
    private Boolean published;
    private ZonedDateTime deadline;
    private int investmentsCount;
    private int questionsCount;
    private String region;
    private String mainIncomeType;
    private Boolean insuranceActive;
    private List<String> insuranceHistory;

    /**
     * To avoid manipulation with floats on the UI, prepare all the data on the server in advance
     *
     * @return revenueRate multiplied by 100 if exists, null otherwise
     */
    @JsonSerialize
    public BigDecimal revenue() {
        return revenueRate != null ? revenueRate.multiply(BigDecimal.valueOf(100)) : null;
    }
}
