package zovryn.finance_dashboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import zovryn.finance_dashboard.filter.JwtAuthFilter;
import zovryn.finance_dashboard.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/users/register", "/api/users/login", "/api/users/bootstrap-admin").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        
                        // Dashboard - all authenticated users
                        .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "ANALYST", "VIEWER")
                        
                        // Only ADMIN can create, update, delete transactions
                        .requestMatchers(HttpMethod.POST, "/api/transactions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/transactions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/transactions/**").hasRole("ADMIN")
                        // ANALYST can only read
                        .requestMatchers(HttpMethod.GET, "/api/transactions/**").hasAnyRole("ADMIN", "ANALYST")
                        // Users - ADMIN only
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        
                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
