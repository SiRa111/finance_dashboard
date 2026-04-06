package zovryn.finance_dashboard.mapper;

import zovryn.finance_dashboard.dto.RegisterResponseDTO;
import zovryn.finance_dashboard.model.UserData;

public class UserMapper {
    /**
     * Helper method to convert the UserData to a Public Response DTO.
     */
    public static RegisterResponseDTO mapToDTO(UserData user) {
        RegisterResponseDTO dto = new RegisterResponseDTO();
        dto.setUser_id(user.getUser_id());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setDate_joined(user.getDate_joined());
        return dto;
    }
}
