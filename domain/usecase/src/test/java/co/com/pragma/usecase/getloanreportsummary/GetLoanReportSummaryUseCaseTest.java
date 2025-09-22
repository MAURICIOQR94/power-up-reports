package co.com.pragma.usecase.getloanreportsummary;

import co.com.pragma.model.loanreport.LoanReport;
import co.com.pragma.model.loanreport.gateways.LoanReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetLoanReportSummaryUseCaseTest {


    @Mock
    private LoanReportRepository loanReportRepository;

    @InjectMocks
    private GetLoanReportSummaryUseCase useCase;

    @Test
    void returnLoanReportSummary() {
        LoanReport report = new LoanReport("APPROVED-LOANS", 5L, new BigDecimal("10000"));
        when(loanReportRepository.findApprovedReport()).thenReturn(Mono.just(report));

        Mono<LoanReport> result = useCase.execute();

        StepVerifier.create(result)
                .expectNext(report)
                .verifyComplete();
    }

    @Test
    void returnLoanReportSummaryEmpty(){
        when(loanReportRepository.findApprovedReport()).thenReturn(Mono.empty());

        Mono<LoanReport> result = useCase.execute();

        StepVerifier.create(result)
                .verifyComplete();
    }
}