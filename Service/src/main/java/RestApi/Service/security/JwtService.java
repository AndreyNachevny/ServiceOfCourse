package RestApi.Service.security;

import RestApi.Service.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtService {
    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    public JwtService(@Value("${access_token}") String accessKey,
                      @Value("${refresh_token}") String refreshKey){
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
    }

    public String generateAccessToken(User user){
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        Date accessExpiration = Date.from(accessExpirationInstant);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(accessExpiration)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user){
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        Date accessExpiration = Date.from(accessExpirationInstant);


        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(accessExpiration)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(refreshKey,SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String accessToken){
        return validateToken(accessToken,accessKey);
    }

    public boolean validateRefreshToken(String refreshToken){
        return validateToken(refreshToken, refreshKey);
    }

    private boolean validateToken(String token, SecretKey key){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build().
                    parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            System.out.println("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            System.out.println("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            System.out.println("Malformed jwt");
        } catch (Exception e) {
            System.out.println("invalid token");
        }
        return false;
    }

    public Claims getAccessClaims(String accessToken){
        return extractClaims(accessToken,accessKey);
    }

    public Claims getRefreshClaims(String refreshToken){
        return extractClaims(refreshToken, refreshKey);
    }

    private Claims extractClaims(String token, SecretKey key){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
