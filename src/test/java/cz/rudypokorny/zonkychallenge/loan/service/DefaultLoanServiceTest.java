package cz.rudypokorny.zonkychallenge.loan.service;

import cz.rudypokorny.zonkychallenge.loan.domain.LoanInfo;
import cz.rudypokorny.zonkychallenge.loan.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultLoanServiceTest {

    @Captor
    private ArgumentCaptor<LoanInfo> loanInfoCaptor;
    @Mock
    private LoanRepository loanRepository;
    @InjectMocks
    private DefaultLoanService service;

    @Test
    void testSaveLoanWhichDoesNotExistsSavesTheLoan() {
        LoanInfo newLoan = LoanInfo.builder()
                .loanId("newLoanId")
                .amount(BigDecimal.ONE)
                .build();

        when(loanRepository.findByLoanId(eq(newLoan.getLoanId()))).thenReturn(Optional.empty());
        when(loanRepository.save(eq(newLoan))).thenReturn(newLoan);

        service.saveLoan(newLoan);

        //verify that the value send to save was the original passed in object
        verify(loanRepository).save(loanInfoCaptor.capture());
        assertEquals(loanInfoCaptor.getValue(), newLoan);
    }

    @Test
    void testSaveLoanWhichDoeExistsUpdatesOnly() {
        LoanInfo newLoan = LoanInfo.builder()
                .loanId("someId")
                .amount(BigDecimal.ONE)
                .build();
        LoanInfo persistedLoam = LoanInfo.builder()
                .loanId("someId")
                .id("id")
                .amount(BigDecimal.ONE)
                .created(LocalDateTime.now())
                .build();

        when(loanRepository.findByLoanId(eq(newLoan.getLoanId()))).thenReturn(Optional.of(persistedLoam));
        when(loanRepository.save(eq(persistedLoam))).thenReturn(persistedLoam);

        service.saveLoan(newLoan);

        //verify that the value send to save was the original passed in object
        verify(loanRepository).save(loanInfoCaptor.capture());
        assertEquals(loanInfoCaptor.getValue(), persistedLoam);
    }
}