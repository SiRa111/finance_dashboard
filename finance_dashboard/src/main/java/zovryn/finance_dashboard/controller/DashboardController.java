package zovryn.finance_dashboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zovryn.finance_dashboard.dto.MonthlyTrendDTO;
import zovryn.finance_dashboard.dto.ResponseCategoryBreakdownDTO;
import zovryn.finance_dashboard.dto.SummaryResponseDTO;
import zovryn.finance_dashboard.dto.TransactionResponseDTO;
import zovryn.finance_dashboard.service.DashboardSummaryService;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Financial dashboard and analytics endpoints")
public class DashboardController {

    @Autowired
    private DashboardSummaryService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Get financial summary", description = "Retrieve overall financial summary including total income, expenses, and balance")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Summary retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - authentication required", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<SummaryResponseDTO> showSummary () {
        SummaryResponseDTO response = dashboardService.getSummary();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category_breakdown")
    @Operation(summary = "Get category breakdown", description = "Retrieve breakdown of expenses by category")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category breakdown retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - authentication required", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<List<ResponseCategoryBreakdownDTO>> showCategoryBreakdown () {
        List<ResponseCategoryBreakdownDTO> response = dashboardService.getCategoryBreakdown();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent_activity")
    @Operation(summary = "Get recent activity", description = "Retrieve the most recent transactions (last 10)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Recent activity retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - authentication required", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<List<TransactionResponseDTO>> showRecent() {
        List<TransactionResponseDTO> response = dashboardService.recentTransactions();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly_breakdown")
    @Operation(summary = "Get monthly trends", description = "Retrieve income and expense trends for a specific month")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Monthly trends retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - authentication required", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "400", description = "Invalid month or year parameter", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<MonthlyTrendDTO> showMonthlyTrends (
            @RequestParam int month,
            @RequestParam int year) {
        MonthlyTrendDTO response = dashboardService.getMonthlyTrends(month, year);
        return ResponseEntity.ok(response);
    }






}
