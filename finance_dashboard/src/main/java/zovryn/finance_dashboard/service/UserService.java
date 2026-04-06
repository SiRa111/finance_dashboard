package zovryn.finance_dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import zovryn.finance_dashboard.dto.*;
import zovryn.finance_dashboard.enums.Role;
import zovryn.finance_dashboard.enums.Status;
import zovryn.finance_dashboard.mapper.UserMapper;
import zovryn.finance_dashboard.model.UserData;
import zovryn.finance_dashboard.repo.UserRepo;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Registers a new user and returns their profile details.
     * Also checks for already existing username or email_id
     */
    public RegisterResponseDTO registerUser(RegisterRequestDTO dto) {

        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepo.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        String hash_pass = encoder.encode(dto.getPassword());
        UserData user = new UserData();
        user.setUsername(dto.getUsername());
        user.setPassword(hash_pass);
        user.setEmail(dto.getEmail());
        user.setRole(Role.VIEWER);
        user.setDate_joined(LocalDateTime.now());
        user.setStatus(Status.ACTIVE);

        UserData saved_user = userRepo.save(user);

        return UserMapper.mapToDTO(saved_user);
    }

    /**
     * Authenticates a user and returns a JWT token.
     */
    public LoginResponseDTO loginUser(LoginRequestDTO dto) throws RuntimeException {
    UserData user = userRepo.findByUsername(dto.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Check if user is inactive
    if (!user.isEnabled()) {
        throw new BadCredentialsException("User account is inactive");
    }

    if (encoder.matches(dto.getPassword(), user.getPassword())) {
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(jwtService.generateToken(user));
        return response;
    } else {
        throw new BadCredentialsException("Invalid Password");
    }
}

    /**
     * Fetches all users as UserDTO.
     */
    public List<RegisterResponseDTO> getAllUsers() {
        List<UserData> allUsersData = userRepo.findAll();

        return allUsersData.stream()
                .map(UserMapper::mapToDTO)
                .toList();
    }

    /**
    * Fetches a user by their user_id
    */
    public RegisterResponseDTO getUserById(int user_id) {
        UserData user = userRepo.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user_id));

        return UserMapper.mapToDTO(user);
    }

    /**
     * Deletes a user by their user_id
     */
    public void deleteUser(int user_id) {
        UserData found_user = userRepo.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.deleteById(user_id);
        System.out.println("User has been deleted. ");
    }

    /**
     * Updates the user's role
     */
    public void updateUserRole(int user_id, Role role) {
            UserData found_user = userRepo.findById(user_id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            found_user.setRole(role);
            userRepo.save(found_user);
            System.out.println("User's role has been updated. ");
    }

     /**
     * Updates user status
     */
     public void updateUserStatus (int user_id, Status status) {
         UserData found_user = userRepo.findById(user_id)
                 .orElseThrow(() -> new RuntimeException("User not found"));
         found_user.setStatus(status);
         userRepo.save(found_user);
         System.out.println("User's status has been updated. ");
     }

    /**
     * Bootstrap the first admin user.
     * The most recently created VIEWER user becomes an ADMIN.
     * If admins already exist, demote them and promote a fresh VIEWER user.
     */
    public LoginResponseDTO bootstrapAdmin() {
    try {
        List<UserData> allUsers = userRepo.findAll();
        
        // Check if bootstrap already completed (admin already exists)
        boolean adminExists = allUsers.stream()
                .anyMatch(user -> user.getRole() == Role.ADMIN);
        
        if (adminExists) {
            throw new RuntimeException("Bootstrap already completed. Admin user(s) already exist.");
        }
        
        // Find first VIEWER user to promote
        UserData viewerUser = null;
        for (UserData user : allUsers) {
            if (user.getRole() == Role.VIEWER) {
                viewerUser = user;
                break;
            }
        }

        if (viewerUser == null) {
            throw new RuntimeException("No VIEWER user found to promote");
        }

        // Promote to ADMIN
        viewerUser.setRole(Role.ADMIN);
        UserData saved = userRepo.save(viewerUser);

        // Generate token
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(jwtService.generateToken(saved));
        return response;
    } catch (Exception e) {
        throw new RuntimeException("Bootstrap failed: " + e.getMessage());
    }
}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
