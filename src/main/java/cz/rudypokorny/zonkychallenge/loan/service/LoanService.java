package cz.rudypokorny.zonkychallenge.loan.service;

import cz.rudypokorny.zonkychallenge.loan.domain.LoanInfo;

import java.util.Optional;

/**
 * Service shielding the operation done on {@link LoanInfo}.
 * Currently transaction support in NOT implemented, as MongoDb requires a replica sets to be setup (unnecessary for this demo).
 */
public interface LoanService {

    /**
     * Saving the loan information. May create new entity or update already persisted entity.
     * If updating the entity, everything except the identifier and auditing information is rewritten.
     *
     * @param newLoan data to be saved
     * @return newly persisted entity
     */
    LoanInfo saveLoan(final LoanInfo newLoan);

    /**
     * Retrieve the {@link LoanInfo} with the latest datePublished wrapped in {@link Optional}.
     *
     * @return {@link Optional} holding the most recently published entity
     */
    Optional<LoanInfo> findMostRecentlyPublished();
}
