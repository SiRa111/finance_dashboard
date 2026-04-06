package zovryn.finance_dashboard.model;

import jakarta.persistence.*;
import lombok.Data;
import zovryn.finance_dashboard.enums.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="transactionData")
@Data
public class TransactionData {
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    int transaction_id;
    BigDecimal amount;
    Type type;
    String category;
    LocalDateTime date;
    String notes;
    @ManyToOne @JoinColumn(name="user_id")
    UserData user_id;
    private boolean is_deleted = false;
}
