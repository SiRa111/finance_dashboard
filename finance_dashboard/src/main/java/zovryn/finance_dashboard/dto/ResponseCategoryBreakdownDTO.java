package zovryn.finance_dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ResponseCategoryBreakdownDTO {
    private String category;
    private BigDecimal expense;
}
