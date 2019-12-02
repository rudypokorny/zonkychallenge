package cz.rudypokorny.zonkychallenge.zonkyapi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.rudypokorny.zonkychallenge.common.ExternalData;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Domain object representing Zonky's loans entity
 */
@Data
public class MarketplaceLoan implements ExternalData {

    //Zonky LOAN entity
    private String id;
    private String url;
    private String name;
    private String story;
    private String purpose;
    //ommiting photos intentionally
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime datePublished;
    private Boolean published;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime deadline;
    private int investmentsCount;
    private int questionsCount;
    private String region;
    private String mainIncomeType;
    private Boolean insuranceActive;
    //ommiting insuranceHistory intentionally

}
