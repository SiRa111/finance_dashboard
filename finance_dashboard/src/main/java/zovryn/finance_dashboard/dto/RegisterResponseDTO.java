package zovryn.finance_dashboard.dto;

import lombok.Data;
import zovryn.finance_dashboard.enums.Role;
import zovryn.finance_dashboard.enums.Status;

import java.time.LocalDateTime;

@Data
public class RegisterResponseDTO {
    private String email;
    private String username;
    private int user_id;
    private Role role;
    private Status status;
    private LocalDateTime date_joined;
}
