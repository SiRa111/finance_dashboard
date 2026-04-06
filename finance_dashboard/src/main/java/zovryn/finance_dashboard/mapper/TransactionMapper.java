package zovryn.finance_dashboard.mapper;

import zovryn.finance_dashboard.dto.TransactionResponseDTO;
import zovryn.finance_dashboard.model.TransactionData;

public class TransactionMapper {

    /**
     *Maps the transaction data to DTO
     */
    public static TransactionResponseDTO mapToDTO (TransactionData transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setAmount(transaction.getAmount());
        dto.setCategory(transaction.getCategory());
        dto.setDate(transaction.getDate());
        dto.setNotes(transaction.getNotes());
        dto.setType(transaction.getType());
        dto.setTransaction_id(transaction.getTransaction_id());
        dto.setUser_id(transaction.getUser_id().getUser_id());
        return dto;
    }
}
