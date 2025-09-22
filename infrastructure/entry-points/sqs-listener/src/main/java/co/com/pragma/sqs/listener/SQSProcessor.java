package co.com.pragma.sqs.listener;

import co.com.pragma.model.LoanReportMessage;
import co.com.pragma.usecase.processloanreportmessage.ProcessLoanReportMessageUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Log4j2
@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {

    private final ProcessLoanReportMessageUseCase processLoanReportMessageUseCase;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> apply(Message message) {
        return Mono.fromCallable(() -> objectMapper.readValue(message.body(), LoanReportMessage.class))
                .flatMap(loanReportMessage -> {
                    log.info("Message received: status={}, amount={}",
                            loanReportMessage.getStatus(), loanReportMessage.getAmount());

                    return processLoanReportMessageUseCase.execute(loanReportMessage);
                });
    }
}
