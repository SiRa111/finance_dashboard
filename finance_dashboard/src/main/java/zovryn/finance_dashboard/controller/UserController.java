package zovryn.finance_dashboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zovryn.finance_dashboard.dto.LoginRequestDTO;
import zovryn.finance_dashboard.dto.LoginResponseDTO;
import zovryn.finance_dashboard.dto.RegisterRequestDTO;
import zovryn.finance_dashboard.dto.RegisterResponseDTO;
import zovryn.finance_dashboard.dto.RoleUpdateDTO;
import zovryn.finance_dashboard.dto.StatusUpdateDTO;
import zovryn.finance_dashboard.enums.Role;
import zovryn.finance_dashboard.enums.Status;
import zovryn.finance_dashboard.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User registration, login, and profile management endpoints")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account with provided credentials. Returns JWT token and user details.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or user already exists", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        RegisterResponseDTO response = userService.registerUser(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with username and password. Returns JWT token for use in authenticated requests.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials or user not active", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = userService.loginUser(dto);
        return  ResponseEntity.ok(response);
    }

    @GetMapping("/allUsers")
    @Operation(summary = "Get all users", description = "Retrieve list of all users (requires ADMIN role)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<List<RegisterResponseDTO>> getAllUsers() {
        List<RegisterResponseDTO> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{user_id}")
    @Operation(summary = "Get user by ID", description = "Retrieve specific user details by user ID (requires ADMIN role)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<RegisterResponseDTO> getUserById(@PathVariable int user_id) {
        RegisterResponseDTO response = userService.getUserById(user_id);
        return  ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{user_id}")
    @Operation(summary = "Delete user", description = "Soft-delete a user (marks as deleted, requires ADMIN role)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<String> deleteUser(@PathVariable int user_id) {
        userService.deleteUser(user_id);
        return ResponseEntity.ok("User has been deleted");
    }

    @PatchMapping("/{user_id}/role")
    @Operation(summary = "Update user role", description = "Change the role of a user (ADMIN, ANALYST, or VIEWER). Requires ADMIN role.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid role value", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<String> updateRole(@PathVariable int user_id, @RequestBody RoleUpdateDTO dto) {
        userService.updateUserRole(user_id, dto.getRole());
        return ResponseEntity.ok("Role updated");
    }

    @PatchMapping("/{user_id}/status")
    @Operation(summary = "Update user status", description = "Change the status of a user (ACTIVE or INACTIVE). Requires ADMIN role.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status value", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<String> updateRole(@PathVariable int user_id, @RequestBody StatusUpdateDTO dto) {
        userService.updateUserStatus(user_id, dto.getStatus());
        return ResponseEntity.ok("Status updated");
    }

    @PostMapping("/bootstrap-admin")
    @Operation(summary = "Bootstrap admin user", description = "Create initial admin user (only works if no admin exists). This is a one-time setup endpoint.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Admin user created successfully"),
        @ApiResponse(responseCode = "400", description = "Bootstrap already completed - admin user already exists", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<LoginResponseDTO> bootstrapAdmin() {
        LoginResponseDTO response = userService.bootstrapAdmin();
        return ResponseEntity.ok(response);
    }
}
