package zovryn.finance_dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zovryn.finance_dashboard.dto.*;
import zovryn.finance_dashboard.enums.Type;
import zovryn.finance_dashboard.mapper.TransactionMapper;
import zovryn.finance_dashboard.mapper.UserMapper;
import zovryn.finance_dashboard.model.TransactionData;
import zovryn.finance_dashboard.model.UserData;
import zovryn.finance_dashboard.repo.TransactionRepo;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardSummaryService {

    @Autowired
    private TransactionRepo transactionRepo;

    /**
     * summarizes the income, expense and the net balance left
     *
     */
    public SummaryResponseDTO getSummary() {
        BigDecimal total_income = transactionRepo.sumByType(Type.INCOME);
        BigDecimal total_expense = transactionRepo.sumByType(Type.EXPENSE);
        
        // Handle null values when no transactions exist
        total_income = total_income != null ? total_income : BigDecimal.ZERO;
        total_expense = total_expense != null ? total_expense : BigDecimal.ZERO;
        
        BigDecimal net_balance = total_income.subtract(total_expense);
        SummaryResponseDTO dto = new SummaryResponseDTO();
        dto.setExpense(total_expense);
        dto.setIncome(total_income);
        dto.setNet_balance(net_balance);
        return dto;
    }

    /**
     * Gets category wise breakdown
     *
     */
    public List<ResponseCategoryBreakdownDTO> getCategoryBreakdown() {
        List<ResponseCategoryBreakdownDTO> categorywiseBreakdown = transactionRepo.findExpenseByCategory();
        return categorywiseBreakdown;
    }

    /**
     * returns the last 10 recent transactions
     */
    public List<TransactionResponseDTO> recentTransactions() {
        List<TransactionData> latestTransactions = transactionRepo.findTop10ByOrderByDateDesc();
        return latestTransactions.stream()
                .map(TransactionMapper::mapToDTO)
                .toList();
    }

    /**
     * returns the monthly trends
     * */
    public MonthlyTrendDTO getMonthlyTrends(int month, int year) {
        BigDecimal monthlyExpense = transactionRepo.sumByTypeAndMonth(Type.EXPENSE, year, month);
        BigDecimal monthlyIncome = transactionRepo.sumByTypeAndMonth(Type.INCOME, year, month);
        
        // Handle null values when no transactions exist for the month
        monthlyExpense = monthlyExpense != null ? monthlyExpense : BigDecimal.ZERO;
        monthlyIncome = monthlyIncome != null ? monthlyIncome : BigDecimal.ZERO;
        
        MonthlyTrendDTO dto = new MonthlyTrendDTO();
        dto.setExpense(monthlyExpense);
        dto.setIncome(monthlyIncome);
        dto.setMonth(year + "-" + String.format("%02d", month));

        return dto;
    }


}