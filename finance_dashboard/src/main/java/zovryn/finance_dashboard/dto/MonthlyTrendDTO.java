package zovryn.finance_dashboard.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyTrendDTO {
    private BigDecimal income;
    private BigDecimal expense;
    private String month;
}
