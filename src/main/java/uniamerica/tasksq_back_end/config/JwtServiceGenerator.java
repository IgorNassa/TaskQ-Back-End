package uniamerica.tasksq_back_end.config;

import java.security.Key;
import java.util.Date;

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
        this.parser = Jwts.parserBuilder().setSigningKey(signingKey).build();
    }

    public String generateToken(User usuario) {
        Date emissao = new Date();
        Date expiracao = new Date(emissao.getTime() + horasExpiracao * 3600000L);
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("nome", usuario.getNome())
                .claim("email", usuario.getEmail())
                .claim("perfil", usuario.getPerfil().name())
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
