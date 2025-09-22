package co.com.pragma.usecase.processloanreportmessage;

import co.com.pragma.common.exception.BusinessException;
import co.com.pragma.model.LoanReportMessage;
import co.com.pragma.model.loanreport.gateways.LoanReportRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static co.com.pragma.common.enums.BusinessExceptionMessage.*;

@RequiredArgsConstructor
public class ProcessLoanReportMessageUseCase {

    private static final String APPROVED = "APROBADO";

    private final LoanReportRepository loanReportRepository;

    public Mono<Void> execute(LoanReportMessage message) {
        return validateMessage(message)
                .filter(this::isApproved)
                .flatMap(validMessage ->
                        loanReportRepository.incrementApprovedCounters(validMessage.getAmount())
                                .onErrorMap(ex -> new BusinessException(FAIL_PROCESSING_REPORT_MESSAGE))
                )
                .then();
    }


    private Mono<LoanReportMessage> validateMessage(LoanReportMessage message) {
        if (message == null) {
            return Mono.error(new BusinessException(MESSAGE_NOT_NULL));
        }
        if (message.getStatus() == null || message.getStatus().trim().isEmpty()) {
            return Mono.error(new BusinessException(MESSAGE_NOT_NULL_OR_EMPTY));
        }
        if (message.getAmount() == null || message.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new BusinessException(INVALID_AMOUNT_VALUE));
        }
        return Mono.just(message);
    }

    private boolean isApproved(LoanReportMessage message) {
        return APPROVED.equalsIgnoreCase(message.getStatus());
    }

}
