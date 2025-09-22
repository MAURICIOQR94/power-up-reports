package co.com.pragma.api.services;

import co.com.pragma.usecase.getloanreportsummary.GetLoanReportSummaryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@RequiredArgsConstructor
public class ReportHandler {

    private final GetLoanReportSummaryUseCase getLoanReportSummaryUseCase;

    public Mono<ServerResponse> getApprovedReport(ServerRequest request) {
        log.info("Obtaining approved loan report");
        return getLoanReportSummaryUseCase.execute()
                .flatMap(report -> ServerResponse.ok().bodyValue(report))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
