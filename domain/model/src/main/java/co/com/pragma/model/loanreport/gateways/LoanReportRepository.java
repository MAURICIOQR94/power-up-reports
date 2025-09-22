package co.com.pragma.model.loanreport.gateways;

import co.com.pragma.model.loanreport.LoanReport;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface LoanReportRepository {

    Mono<LoanReport> findApprovedReport();
    Mono<LoanReport> incrementApprovedCounters(BigDecimal amount);
}
