package uniamerica.tasksq_back_end.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uniamerica.tasksq_back_end.entity.User;

@Service
public class JwtServiceGenerator {

    private static final SignatureAlgorithm ALGORITMO_ASSINATURA = SignatureAlgorithm.HS256;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiracao}")
    private long horasExpiracao;

    public Map<String, Object> gerarPayload(User usuario) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", usuario.getId());
        payload.put("nome", usuario.getNome());
        payload.put("email", usuario.getEmail());
        payload.put("perfil", usuario.getPerfil().name());
        return payload;
    }

    public String generateToken(User usuario) {
        Date emissao = new Date();
        Date expiracao = new Date(emissao.getTime() + horasExpiracao * 3600000L);
        return Jwts.builder()
                .setClaims(gerarPayload(usuario))
                .setSubject(usuario.getEmail())
                .setIssuedAt(emissao)
                .setExpiration(expiracao)
                .signWith(getSigningKey(), ALGORITMO_ASSINATURA)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
