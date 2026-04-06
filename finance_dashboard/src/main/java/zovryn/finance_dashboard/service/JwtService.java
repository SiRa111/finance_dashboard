package zovryn.finance_dashboard.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zovryn.finance_dashboard.model.UserData;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    //generates token
    public String generateToken(UserData user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    //single method to extract data
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //extracts username
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    //extracts role
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    //extracts the legitimacy of the token
    public boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    //validates token
    public Boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        Boolean expired_token = isTokenExpired(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

}
