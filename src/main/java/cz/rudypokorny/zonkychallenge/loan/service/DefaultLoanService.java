package cz.rudypokorny.zonkychallenge.loan.service;

import cz.rudypokorny.zonkychallenge.loan.domain.LoanInfo;
import cz.rudypokorny.zonkychallenge.loan.repository.LoanRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class DefaultLoanService implements LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public DefaultLoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public LoanInfo saveLoan(final LoanInfo newLoan) {
        final LoanInfo loamToBeSaved = loanRepository.findByLoanId(newLoan.getLoanId())
                .map(saved -> updateTheLoan(saved, newLoan))
                .orElseGet(() -> {
                    log.info(String.format("Saving the item ID: %s, nickname: %s", newLoan.getLoanId(), newLoan.getNickName()));
                    return newLoan;
                });

        final LoanInfo savedLoan = loanRepository.save(loamToBeSaved);

        log.debug(String.format("Loan saved: %s", savedLoan.toString()));
        return savedLoan;
    }

    @Override
    public Optional<LoanInfo> findMostRecentlyPublished() {
        return loanRepository.findTopByOrderByDatePublishedDesc();
    }


    /**
     * Overwrite everything except the ID and creation date
     * @param saved
     * @param newLoan
     * @return
     */
    private LoanInfo updateTheLoan(final LoanInfo saved, final LoanInfo newLoan) {
        newLoan.setId(saved.getId());
        newLoan.setCreated(saved.getCreated());

        log.info(String.format("Loan with given ID %s already exists, updating.", saved.getId()));
        return newLoan;
    }
}
