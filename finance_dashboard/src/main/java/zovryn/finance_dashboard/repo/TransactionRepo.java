package zovryn.finance_dashboard.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zovryn.finance_dashboard.dto.ResponseCategoryBreakdownDTO;
import zovryn.finance_dashboard.enums.Type;
import zovryn.finance_dashboard.model.TransactionData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepo extends JpaRepository<TransactionData, Integer> {

    @Query("SELECT t FROM TransactionData t WHERE " +
            "(:category IS NULL OR t.category = :category) AND " +
            "(:type IS NULL OR t.type = :type) AND " +
            "(:date IS NULL OR t.date = :date) AND " +
            "t.is_deleted = false")
    Page<TransactionData> findWithFilters(@Param("category") String category,
                                          @Param("type") Type type,
                                          @Param("date") LocalDateTime date,
                                          Pageable pageable);

    @Query("SELECT new zovryn.finance_dashboard.dto.ResponseCategoryBreakdownDTO(t.category, SUM(t.amount)) " +
            "FROM TransactionData t WHERE t.type = zovryn.finance_dashboard.enums.Type.EXPENSE AND t.is_deleted = false GROUP BY t.category")
    List<ResponseCategoryBreakdownDTO> findExpenseByCategory();

    @Query("SELECT SUM(t.amount) FROM TransactionData t WHERE t.type = :type AND t.is_deleted = false")
    BigDecimal sumByType(@Param("type") Type type);

    @Query("SELECT t FROM TransactionData t WHERE t.is_deleted = false ORDER BY t.date DESC")
    List<TransactionData> findTop10ByOrderByDateDesc();

    @Query("SELECT SUM(t.amount) FROM TransactionData t WHERE t.type = :type AND YEAR(t.date) = :year AND MONTH(t.date) = :month AND t.is_deleted = false")
    BigDecimal sumByTypeAndMonth(@Param("type") Type type, @Param("year") int year, @Param("month") int month);
}
