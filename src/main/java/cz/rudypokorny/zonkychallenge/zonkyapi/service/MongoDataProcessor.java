package cz.rudypokorny.zonkychallenge.zonkyapi.service;

import cz.rudypokorny.zonkychallenge.loan.domain.LoanInfo;
import cz.rudypokorny.zonkychallenge.common.DataProcessor;
import cz.rudypokorny.zonkychallenge.zonkyapi.domain.MarketplaceLoan;
import cz.rudypokorny.zonkychallenge.exceptions.ConversionException;
import cz.rudypokorny.zonkychallenge.exceptions.ConverterNotFoundException;
import cz.rudypokorny.zonkychallenge.common.ConverterFactory;
import cz.rudypokorny.zonkychallenge.loan.service.DefaultLoanService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Qualifier("zonkyLoansMongoDataProcessor")
public class MongoDataProcessor implements DataProcessor<MarketplaceLoan> {

    public static final String PROCESSOR_NAME = "MongoDB";

    private final DefaultLoanService loanService;
    private final ConverterFactory converterFactory;

    @Autowired
    public MongoDataProcessor(DefaultLoanService loanService, ConverterFactory converterFactory) {
        this.loanService = loanService;
        this.converterFactory = converterFactory;
    }


    @Override
    public String getName() {
        return PROCESSOR_NAME;
    }

    @Override
    public void process(final MarketplaceLoan data) {
        try {
            //convert the item to domain object
            LoanInfo loan = (LoanInfo) converterFactory.findConverter(data).convert(data);

            loanService.saveLoan(loan);

        } catch (ConverterNotFoundException | ConversionException e) {
            log.error(String.format("Conversion for loanId '%s' had failed. %s", data.getId(), e.getMessage()));
        }
    }

}
