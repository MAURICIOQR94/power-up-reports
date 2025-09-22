package co.com.pragma.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReportResponseDTO {

    private Long totalApproved;
    private BigDecimal totalAmount;
}
