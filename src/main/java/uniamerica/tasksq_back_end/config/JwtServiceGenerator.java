package uniamerica.tasksq_back_end.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uniamerica.tasksq_back_end.entity.User;

@Service
public class JwtServiceGenerator {

    private static final SignatureAlgorithm ALGORITMO_ASSINATURA = SignatureAlgorithm.HS256;
    private static final String EMISSOR = "task-q";
    private static final String AUDIENCIA = "task-q-api";

    private final Key signingKey;
    private final JwtParser parser;
    private final long horasExpiracao;

    public JwtServiceGenerator(@Value("${jwt.secret}") String secret,
                               @Value("${jwt.expiracao}") long horasExpiracao) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        if (horasExpiracao <= 0) {
            throw new IllegalArgumentException("jwt.expiracao deve ser maior que zero");
        }
        this.horasExpiracao = horasExpiracao;
        this.parser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .requireIssuer(EMISSOR)
                .requireAudience(AUDIENCIA)
                .build();
    }

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
                .setIssuer(EMISSOR)
                .setAudience(AUDIENCIA)
                .setIssuedAt(emissao)
                .setExpiration(expiracao)
                .signWith(signingKey, ALGORITMO_ASSINATURA)
                .compact();
    }

    public Long extractUserId(String token) {
        Jws<Claims> jwt = parser.parseClaimsJws(token);
        if (!ALGORITMO_ASSINATURA.getValue().equals(jwt.getHeader().getAlgorithm())) {
            throw new IllegalArgumentException("Algoritmo JWT inválido");
        }
        Claims claims = jwt.getBody();
        if (claims.getExpiration() == null) {
            throw new IllegalArgumentException("JWT sem expiração");
        }
        return claims.get("id", Long.class);
    }
}
