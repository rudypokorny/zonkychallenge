package cz.rudypokorny.zonkychallenge.loan.repository;

import cz.rudypokorny.zonkychallenge.loan.domain.LoanInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends PagingAndSortingRepository<LoanInfo, String> {

    Optional<LoanInfo> findByLoanId(final String loanId);

    Optional<LoanInfo> findFirstByOrderByDatePublished();

    Optional<LoanInfo> findTopByOrderByDatePublishedDesc();
}
