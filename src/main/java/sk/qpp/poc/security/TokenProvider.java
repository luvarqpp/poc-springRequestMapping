package sk.qpp.poc.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * NOTE: JwtBuilder and JwtParser seems to work with Date (milliseconds resolution), but actually jwt token does
 * include time with only second resolution.
 */
@Slf4j
@Service
public class TokenProvider {
    private final JwtParser jwtParser;
    private final JwtBuilder jwtBuilder;
    private final long expirationMilliseconds;
    /**
     * This is source for {@link JwtBuilder#setIssuedAt(Date)} call. It is same clock source as is used
     * in {@link JwtParser} instance for validating token when {@link JwtParser#parseClaimsJws(String)} is called.
     */
    private final Clock clock = DefaultClock.INSTANCE;

    public TokenProvider() {
        expirationMilliseconds = 10000 * 1000L;
        // SomeAppProperties.getAuth().getTokenSecret()
        final byte[] signingKey = {0x42};
        jwtParser = Jwts.parser()
                .setSigningKey(signingKey);
        jwtBuilder = Jwts.builder()
                // It seems that compression produces longer results. Re-consider if we put into claims something more
                // than just bare minimum.
                //.compressWith(new GzipCompressionCodec())
                .signWith(SignatureAlgorithm.HS512, signingKey);
    }

    public String createToken(final Long userPrincipalId) {
        final Date now = clock.now();
        final Date expiryDate = new Date(now.getTime() + this.expirationMilliseconds);

        return jwtBuilder
                .setSubject(Long.toString(userPrincipalId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = jwtParser
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature. authToken=" + authToken, ex);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token. authToken=" + authToken, ex);
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token. authToken=" + authToken, ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token. authToken=" + authToken, ex);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty. authToken=" + authToken, ex);
        }
        return false;
    }
}
