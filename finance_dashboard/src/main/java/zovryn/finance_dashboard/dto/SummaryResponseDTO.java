package zovryn.finance_dashboard.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SummaryResponseDTO {
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal net_balance;
}
