package cz.rudypokorny.zonkychallenge.zonkyapi.service;

import cz.rudypokorny.zonkychallenge.common.DataConverter;
import cz.rudypokorny.zonkychallenge.common.ConverterFactory;
import cz.rudypokorny.zonkychallenge.common.ExternalData;
import cz.rudypokorny.zonkychallenge.exceptions.ConversionException;
import cz.rudypokorny.zonkychallenge.exceptions.ConverterNotFoundException;
import cz.rudypokorny.zonkychallenge.loan.service.LoanService;
import cz.rudypokorny.zonkychallenge.zonkyapi.domain.MarketplaceLoan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoDataProcessorTest {

    @InjectMocks
    private MongoDataProcessor processor;
    @Mock
    private LoanService loanService;
    @Mock
    private ConverterFactory converterFactory;

    @Test
    void testProcessWithoutConverters() throws ConverterNotFoundException {
        MarketplaceLoan data = new MarketplaceLoan();

        when(converterFactory.findConverter(ArgumentMatchers.eq(data))).thenThrow(new ConverterNotFoundException(MarketplaceLoan.class));

        processor.process(data);

        verify(loanService, never()).saveLoan(any());
    }

    @Test
    void testProcessWithConversionError() throws ConverterNotFoundException {
        MarketplaceLoan data = new MarketplaceLoan();
        DataConverter dummyConverter = new DataConverter() {
            @Override
            public Object convert(ExternalData input) throws ConversionException {
                throw new ConversionException("invalid something");
            }

            @Override
            public boolean supportsConversion(Object from) {
                return true;
            }
        };

        when(converterFactory.findConverter(ArgumentMatchers.eq(data))).thenReturn(dummyConverter);

        processor.process(data);

        verify(loanService, never()).saveLoan(any());
    }
}