package zovryn.finance_dashboard.dto;

import lombok.Data;
import zovryn.finance_dashboard.enums.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {
    private BigDecimal amount;
    private Type type;
    private String category;
    private String notes;
    LocalDateTime date;
    private int user_id;
    private int transaction_id;
}
