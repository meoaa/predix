package project.predix.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;

import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {
    private final SecretKey key;
    private final long accessTtl;
    private final long refreshTtl;

    public JwtProvider(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
        this.accessTtl = props.accessTtl();
        this.refreshTtl = props.refreshTtl();
    }

    public String generateAccess(String username, Collection<? extends GrantedAuthority> auths){
        return build(username, accessTtl, auths);
    }

    public String generateRefresh(String username){
        return build(username, refreshTtl, List.of());
    }

    private String build(
            String sub,
            long ttl,
            Collection<? extends GrantedAuthority> auths) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(sub)
                .claim("roles", auths.stream().map(GrantedAuthority::getAuthority).toList())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(ttl)))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims parse(String token) throws JwtException{
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

    public long getAccessTtl() {
        return accessTtl;
    }

    public long getRefreshTtl() {
        return refreshTtl;
    }
}
