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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zovryn.finance_dashboard.dto.TransactionRequestDTO;
import zovryn.finance_dashboard.dto.TransactionResponseDTO;
import zovryn.finance_dashboard.enums.Type;
import zovryn.finance_dashboard.model.TransactionData;
import zovryn.finance_dashboard.model.UserData;
import zovryn.finance_dashboard.repo.TransactionRepo;
import zovryn.finance_dashboard.service.JwtService;
import zovryn.finance_dashboard.service.TransactionService;
import zovryn.finance_dashboard.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Management", description = "Create, read, update, and delete financial transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @Operation(summary = "Create new transaction", description = "Add a new financial transaction (requires ADMIN role)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid transaction data", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<TransactionResponseDTO> addTransaction(
            @RequestBody TransactionRequestDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        UserData user = (UserData) userService.loadUserByUsername(username);

        TransactionResponseDTO response = transactionService.addTransaction(user.getUser_id(), dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/{transaction_id}")
    @Operation(summary = "Update transaction", description = "Modify an existing transaction (requires ADMIN role, owner can only update own)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid transaction data", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<TransactionResponseDTO> updateTransaction (
            @RequestBody TransactionRequestDTO dto,
            @PathVariable int transaction_id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        UserData user = (UserData) userService.loadUserByUsername(username);

        TransactionResponseDTO response = transactionService.updateTransaction(user.getUser_id(), transaction_id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{transaction_id}")
    @Operation(summary = "Delete transaction", description = "Soft-delete a transaction (requires ADMIN role, owner can only delete own)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<String> deleteTransaction(@PathVariable int transaction_id,
                                                    @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        UserData user = (UserData) userService.loadUserByUsername(username);
        
        transactionService.deleteTransaction(user.getUser_id(), transaction_id);
        return ResponseEntity.ok("Transaction deleted");
    }

    @GetMapping ("/details/{transaction_id}")
    @Operation(summary = "Get transaction details", description = "Retrieve full details of a specific transaction (requires ANALYST or ADMIN role)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ANALYST or ADMIN role", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<TransactionResponseDTO> getByTransactionId (@PathVariable int transaction_id) {
        TransactionResponseDTO response = transactionService.getTransaction(transaction_id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/applyFilters")
    @Operation(summary = "Filter transactions", description = "Retrieve transactions with optional filtering by category, type, and date (requires ANALYST or ADMIN role)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ANALYST or ADMIN role", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<List<TransactionResponseDTO>> getByFilters (
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<TransactionResponseDTO> response = transactionService.getTransactions(category, type, date, page, size);
        return ResponseEntity.ok(response);
    }



}
