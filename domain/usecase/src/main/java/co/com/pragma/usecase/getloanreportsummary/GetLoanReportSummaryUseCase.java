package co.com.pragma.usecase.getloanreportsummary;

import co.com.pragma.model.loanreport.LoanReport;
import co.com.pragma.model.loanreport.gateways.LoanReportRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetLoanReportSummaryUseCase {

    private final LoanReportRepository loanReportRepository;

    public Mono<LoanReport> execute() {
        return loanReportRepository.findApprovedReport();
    }
}
