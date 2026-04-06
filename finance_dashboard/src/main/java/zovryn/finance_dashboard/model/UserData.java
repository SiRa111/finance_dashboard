package zovryn.finance_dashboard.model;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import zovryn.finance_dashboard.enums.Role;
import zovryn.finance_dashboard.enums.Status;

@Data
@Entity
@Table(name="userData")

public class UserData implements UserDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    int user_id;
    String username;
    String password;
    String email;
    @Enumerated(EnumType.STRING)
    Role role;
    @Enumerated(EnumType.STRING)
    Status status;

    LocalDateTime date_joined;

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return status == Status.ACTIVE; }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}