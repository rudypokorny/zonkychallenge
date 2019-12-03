package cz.rudypokorny.zonkychallenge.loan.web;

import cz.rudypokorny.zonkychallenge.loan.domain.LoanInfo;
import cz.rudypokorny.zonkychallenge.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanInfoRestController extends AbstractController {

    public static final String API_LOANS_URL = API_BASE_URL + "/loans";

    private final LoanService loanService;

    @Autowired
    public LoanInfoRestController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping(API_LOANS_URL)
    public ResponseEntity<Page<LoanInfo>> findAll() {
        return ResponseEntity.ok(loanService.findAll(Pageable.unpaged()));
    }

}
