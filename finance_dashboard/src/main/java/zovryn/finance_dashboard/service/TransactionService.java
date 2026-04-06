package zovryn.finance_dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import zovryn.finance_dashboard.dto.TransactionRequestDTO;
import zovryn.finance_dashboard.dto.TransactionResponseDTO;
import zovryn.finance_dashboard.enums.Type;
import zovryn.finance_dashboard.mapper.TransactionMapper;
import zovryn.finance_dashboard.model.TransactionData;
import zovryn.finance_dashboard.model.UserData;
import zovryn.finance_dashboard.repo.TransactionRepo;
import zovryn.finance_dashboard.repo.UserRepo;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private UserRepo userRepo;

    /**
     * Adding Transaction data
     * */
    public TransactionResponseDTO addTransaction (int user_id, TransactionRequestDTO dto) {
        UserData user = userRepo.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        TransactionData transactionData = new TransactionData();
        transactionData.setUser_id(user);
        transactionData.setAmount(dto.getAmount());
        transactionData.setCategory(dto.getCategory());
        transactionData.setDate(dto.getDate());
        transactionData.setType(dto.getType());
        transactionData.setNotes(dto.getNotes());

        TransactionData saved_data = transactionRepo.save(transactionData);
        return TransactionMapper.mapToDTO(saved_data);
    }

    /**
     * Update the transaction
     */
    public TransactionResponseDTO updateTransaction(int userId, int transaction_id, TransactionRequestDTO dto) {
        validateOwnership(transaction_id, userId);
        
        TransactionData transactionData = transactionRepo.findById(transaction_id)
                .orElseThrow(() ->new RuntimeException("Transaction not found. "));
        transactionData.setAmount(dto.getAmount());
        transactionData.setCategory(dto.getCategory());
        transactionData.setDate(dto.getDate());
        transactionData.setNotes(dto.getNotes());
        transactionData.setType(dto.getType());

        TransactionData saved_data = transactionRepo.save(transactionData);
        return TransactionMapper.mapToDTO(saved_data);
    }

    /**
     * Soft Deletes the transaction
     * */
    public void deleteTransaction (int userId, int transaction_id) {
        validateOwnership(transaction_id, userId);
        
        TransactionData transactionData = transactionRepo.findById(transaction_id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transactionData.set_deleted(true);
        transactionRepo.save(transactionData);
        System.out.println("The transaction has  been deleted. ");
    }

    /**
     * Gets the transaction data by Id
     * */
    public TransactionResponseDTO getTransaction (int transaction_id) {
        TransactionData transactionData = transactionRepo.findById(transaction_id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return TransactionMapper.mapToDTO(transactionData);
    }

    /**
     * Get data based on filters
     * */
    public List<TransactionResponseDTO>  getTransactions(String category, Type type, LocalDateTime date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionData> results = transactionRepo.findWithFilters(category, type, date, pageable);
        return results.getContent().stream()
                .map(TransactionMapper::mapToDTO)
                .toList();
    }

    public void validateOwnership(int transactionId, int userId) {
    TransactionData transaction = transactionRepo.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
    
    if (transaction.getUser_id().getUser_id() != userId) {
        throw new AccessDeniedException("Cannot modify other user's transactions");
    }
}


}
