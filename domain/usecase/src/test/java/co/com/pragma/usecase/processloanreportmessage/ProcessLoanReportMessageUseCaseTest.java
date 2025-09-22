package co.com.pragma.usecase.processloanreportmessage;

import co.com.pragma.common.exception.BusinessException;
import co.com.pragma.model.LoanReportMessage;
import co.com.pragma.model.loanreport.gateways.LoanReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static co.com.pragma.common.enums.BusinessExceptionMessage.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessLoanReportMessageUseCaseTest {

    private static final String APPROVED = "APROBADO";
    private static final String REJECTED = "REJECTED";

    @Mock
    private LoanReportRepository loanReportRepository;

    @InjectMocks
    private ProcessLoanReportMessageUseCase useCase;

    @Test
    void shouldNotIncrementWhenStatusIsNotApproved() {
        LoanReportMessage message = LoanReportMessage.builder()
                .status(REJECTED)
                .amount(BigDecimal.TEN)
                .build();

        Mono<Void> result = useCase.execute(message);

        StepVerifier.create(result)
                .verifyComplete();

        verify(loanReportRepository, never()).incrementApprovedCounters(any());
    }

    @Test
    void failWhenMessageIsNull() {
        StepVerifier.create(useCase.execute(null))
                .expectErrorSatisfies(ex -> {
                    assert ex instanceof BusinessException;
                    assert ((BusinessException) ex).getBusinessExceptionMessage() == MESSAGE_NOT_NULL;
                })
                .verify();
    }

    @Test
    void failWhenStatusIsEmpty() {
        LoanReportMessage message = LoanReportMessage.builder()
                .status("")
                .amount(BigDecimal.TEN)
                .build();

        StepVerifier.create(useCase.execute(message))
                .expectErrorSatisfies(ex -> {
                    assert ex instanceof BusinessException;
                    assert ((BusinessException) ex).getBusinessExceptionMessage() == MESSAGE_NOT_NULL_OR_EMPTY;
                })
                .verify();
    }

    @Test
    void failWhenAmountIsInvalid() {
        LoanReportMessage message = LoanReportMessage.builder()
                .status(APPROVED)
                .amount(BigDecimal.ZERO)
                .build();

        StepVerifier.create(useCase.execute(message))
                .expectErrorSatisfies(ex -> {
                    assert ex instanceof BusinessException;
                    assert ((BusinessException) ex).getBusinessExceptionMessage() == INVALID_AMOUNT_VALUE;
                })
                .verify();
    }

    @Test
    void failWhenRepositoryFails() {
        LoanReportMessage message = LoanReportMessage.builder()
                .status(APPROVED)
                .amount(BigDecimal.ONE)
                .build();

        when(loanReportRepository.incrementApprovedCounters(BigDecimal.ONE))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(message))
                .expectErrorSatisfies(ex -> {
                    assert ex instanceof BusinessException;
                    assert ((BusinessException) ex).getBusinessExceptionMessage() == FAIL_PROCESSING_REPORT_MESSAGE;
                })
                .verify();
    }

}