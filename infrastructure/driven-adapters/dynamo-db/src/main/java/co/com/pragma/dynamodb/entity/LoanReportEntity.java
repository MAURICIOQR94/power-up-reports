package co.com.pragma.dynamodb.entity;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.math.BigDecimal;

@Getter
@Setter
@DynamoDbBean
public class LoanReportEntity {

    private String meter;
    private Long totalApproved;
    private BigDecimal totalAmount;

    @DynamoDbPartitionKey
    public String getMeter() {
        return meter;
    }
}
