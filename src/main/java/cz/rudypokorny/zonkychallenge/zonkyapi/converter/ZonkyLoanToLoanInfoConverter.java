package cz.rudypokorny.zonkychallenge.zonkyapi.converter;

import cz.rudypokorny.zonkychallenge.common.DataConverter;
import cz.rudypokorny.zonkychallenge.exceptions.ConversionException;
import cz.rudypokorny.zonkychallenge.loan.domain.LoanInfo;
import cz.rudypokorny.zonkychallenge.zonkyapi.domain.MarketplaceLoan;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZonkyLoanToLoanInfoConverter implements DataConverter<LoanInfo, MarketplaceLoan> {

    /**
     * Map only a subset of the values as we are not interested of all of them
     *
     * @param input object to be converted
     * @return {@link LoanInfo} with just important information about the loan
     * @throws ConversionException
     */
    @Override
    public LoanInfo convert(MarketplaceLoan input) throws ConversionException {
        try {
            validate(input);
            return doConvert(input);
        } catch (ConversionException e) {
            throw e;
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public boolean supportsConversion(final Object toConvert) {
        return toConvert != null && MarketplaceLoan.class.isAssignableFrom(toConvert.getClass());
    }

    /**
     * Validates the data prior saving - just an example of what could be achieved here.
     * Ideally exception should be thrown only if there is no other way ho to recover from this state.
     *
     * @param input input data prior saving
     * @throws ConversionException if the input data is not according to our needs
     */
    private void validate(final MarketplaceLoan input) throws ConversionException {
        if (Objects.isNull(input.getId())) {
            throw new ConversionException("Loan ID is missing");
        }
    }

    private LoanInfo doConvert(final MarketplaceLoan input) {
        return LoanInfo.builder()
                .loanId(input.getId())
                .url(input.getUrl())
                .name(input.getName())
                .story(input.getStory())
                .purpose(input.getPurpose())
                .nickName(input.getNickName())
                .termInMonths(input.getTermInMonths())
                .interestRate(input.getInterestRate())
                .revenueRate(input.getRevenueRate())
                .annuityWithInsurance(input.getAnnuityWithInsurance())
                .rating(input.getRating())
                .topped(input.getTopped())
                .amount(input.getAmount())
                .countryOfOrigin(input.getCountryOfOrigin())
                .currency(input.getCurrency())
                .remainingInvestment(input.getRemainingInvestment())
                .reservedAmount(input.getReservedAmount())
                .investmentRate(input.getInvestmentRate())
                .covered(input.getCovered())
                .datePublished(input.getDatePublished())
                .published(input.getPublished())
                .deadline(input.getDeadline())
                .investmentsCount(input.getInvestmentsCount())
                .questionsCount(input.getQuestionsCount())
                .region(input.getRegion())
                .mainIncomeType(input.getMainIncomeType())
                .insuranceActive(input.getInsuranceActive())
                .build();
    }

}
