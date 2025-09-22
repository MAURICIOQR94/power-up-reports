package co.com.pragma.dynamodb;

import co.com.pragma.dynamodb.entity.LoanReportEntity;
import co.com.pragma.dynamodb.helper.TemplateAdapterOperations;
import co.com.pragma.model.loanreport.LoanReport;
import co.com.pragma.model.loanreport.gateways.LoanReportRepository;
import lombok.extern.log4j.Log4j2;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.math.BigDecimal;
import java.util.Map;

@Log4j2
@Repository
public class LoanReportAdapter extends TemplateAdapterOperations<LoanReport, String, LoanReportEntity> implements LoanReportRepository {

    private static final String TABLE_NAME = "loan_report";
    private static final String METRIC_TYPE = "APPROVED_LOANS";

    private final DynamoDbAsyncClient rawClient;

    public LoanReportAdapter(DynamoDbEnhancedAsyncClient enhancedClient,
                             DynamoDbAsyncClient rawClient,
                             ObjectMapper mapper) {
        super(enhancedClient, mapper, entity -> mapper.map(entity, LoanReport.class), TABLE_NAME);
        this.rawClient = rawClient;
    }

    @Override
    public Mono<LoanReport> findApprovedReport() {
        return super.getById(METRIC_TYPE)
                .switchIfEmpty(Mono.just(createDefaultReport()));
    }

    @Override
    public Mono<LoanReport> incrementApprovedCounters(BigDecimal amount) {
        log.info("Incrementing approved loan report counters");
        Map<String, AttributeValue> key = Map.of(
                "meter", AttributeValue.builder().s(METRIC_TYPE).build()
        );

        Map<String, AttributeValue> values = Map.of(
                ":one", AttributeValue.builder().n("1").build(),
                ":amount", AttributeValue.builder().n(amount.toString()).build()
        );

        Map<String, String> names = Map.of(
                "#totalApproved", "totalApproved",
                "#totalAmount", "totalAmount"
        );

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .updateExpression("ADD #totalApproved :one, #totalAmount :amount")
                .expressionAttributeValues(values)
                .expressionAttributeNames(names)
                .returnValues(ReturnValue.ALL_NEW)
                .build();

        return Mono.fromFuture(rawClient.updateItem(request))
                .map(response -> mapFromDynamoAttributes(response.attributes()))
                .onErrorResume(ex -> {
                    if (ex instanceof ConditionalCheckFailedException
                            || ex instanceof ResourceNotFoundException) {
                        return createFirstRecord(amount);
                    }
                    return Mono.error(ex);
                });
    }

    private Mono<LoanReport> createFirstRecord(BigDecimal amount) {
        LoanReport firstRecord = LoanReport.builder()
                .meter(METRIC_TYPE)
                .totalApproved(1L)
                .totalAmount(amount)
                .build();

        return super.save(firstRecord);
    }

    private LoanReport createDefaultReport() {
        return LoanReport.builder()
                .meter(METRIC_TYPE)
                .totalApproved(0L)
                .totalAmount(BigDecimal.ZERO)
                .build();
    }

    private LoanReport mapFromDynamoAttributes(Map<String, AttributeValue> attributes) {
        return LoanReport.builder()
                .meter(attributes.get("meter").s())
                .totalApproved(Long.valueOf(attributes.get("totalApproved").n()))
                .totalAmount(new BigDecimal(attributes.get("totalAmount").n()))
                .build();
    }

}
