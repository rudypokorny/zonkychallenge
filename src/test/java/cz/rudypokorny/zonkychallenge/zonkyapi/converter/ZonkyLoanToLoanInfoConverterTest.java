package cz.rudypokorny.zonkychallenge.zonkyapi.converter;

import cz.rudypokorny.zonkychallenge.common.DataConverter;
import cz.rudypokorny.zonkychallenge.exceptions.ConversionException;
import cz.rudypokorny.zonkychallenge.loan.domain.LoanInfo;
import cz.rudypokorny.zonkychallenge.zonkyapi.domain.MarketplaceLoan;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZonkyLoanToLoanInfoConverterTest {

    private DataConverter<LoanInfo, MarketplaceLoan> converter = new ZonkyLoanToLoanInfoConverter();

    @Test
    public void testSupportConversion() {
        boolean expectedResult = converter.supportsConversion(new MarketplaceLoan());
        assertTrue(expectedResult);
    }

    @Test
    public void testSupportConversionForChild() {
        boolean expectedResult = converter.supportsConversion(new ChildOfZonkyLoan());
        assertTrue(expectedResult);
    }

    @Test
    public void testSupportConversionFoNull() {
        boolean expectedResult = converter.supportsConversion(null);
        assertFalse(expectedResult);
    }

    @Test
    public void testConvertWithNull() {
        assertThrows(ConversionException.class, () -> converter.convert(null));
    }

    @Test
    public void testConvert() throws ConversionException {
        EasyRandom generator = new EasyRandom();
        MarketplaceLoan actual = generator.nextObject(MarketplaceLoan.class);

        LoanInfo expected = converter.convert(actual);

        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getAnnuityWithInsurance(), actual.getAnnuityWithInsurance());
        assertEquals(expected.getCountryOfOrigin(), actual.getCountryOfOrigin());
        assertEquals(expected.getCovered(), actual.getCovered());
        assertEquals(expected.getCurrency(), actual.getCurrency());
        assertEquals(expected.getDatePublished(), actual.getDatePublished());
        assertEquals(expected.getDeadline(), actual.getDeadline());
        assertEquals(expected.getInsuranceActive(), actual.getInsuranceActive());
        assertEquals(expected.getInterestRate(), actual.getInterestRate());
        assertEquals(expected.getInvestmentsCount(), actual.getInvestmentsCount());
        assertEquals(expected.getLoanId(), actual.getId());
        assertEquals(expected.getMainIncomeType(), actual.getMainIncomeType());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getNickName(), actual.getNickName());
        assertEquals(expected.getPublished(), actual.getPublished());
        assertEquals(expected.getPurpose(), actual.getPurpose());
        assertEquals(expected.getRating(), actual.getRating());
        assertEquals(expected.getRegion(), actual.getRegion());
        assertEquals(expected.getQuestionsCount(), actual.getQuestionsCount());
        assertEquals(expected.getTopped(), actual.getTopped());
        assertEquals(expected.getTermInMonths(), actual.getTermInMonths());
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getRevenueRate(), actual.getRevenueRate());
        assertEquals(expected.getStory(), actual.getStory());
        assertEquals(expected.getRemainingInvestment(), actual.getRemainingInvestment());
        assertEquals(expected.getReservedAmount(), actual.getReservedAmount());
        assertEquals(expected.getInterestRate(), actual.getInterestRate());
        assertEquals(expected.getInvestmentRate(), actual.getInvestmentRate());
    }

    class ChildOfZonkyLoan extends MarketplaceLoan {
        private String someNewAttribute;
    }
}