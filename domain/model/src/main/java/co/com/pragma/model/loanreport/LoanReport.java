package co.com.pragma.model.loanreport;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoanReport {

    private String meter;
    private Long totalApproved;
    private BigDecimal totalAmount;

}
