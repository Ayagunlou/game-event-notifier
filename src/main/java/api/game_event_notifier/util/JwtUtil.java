package api.game_event_notifier.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String jwtSecret = "2370866dba370ac6d00fdb390951bbbeb3309d2fc2e491a9462e07f533e6900cf84971a264ae90a44f2b40e185b3903ac6a64afa8c002699953a032c53baeea58b0feb2ef2b40ae23663627c922209b38f5fdc2a2a73db00ae0d602e1352f67d086794a4e48ba18df6594074390e99604f67d345bb50f9e3bedfa1b18b436bc904c83a23115f2c0ca591227085e966eeb7daf8743d2961ab9f9c6bde13bbd2c594caca67e24e8764da44220e22b8532f482347941630e0cf45cb4bd5377b8007b2e5988efa4d30dd5de9405e83fd8de62cac6fb0a8b5a360965f9b257dfb74ff00711e30fe092209ac3db525ba7efd05dd0353f7e39135895b717368648ad797";
    private final long jwtExpirationMs = 86400000; // 1 วัน

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(jwtSecret);
    }

    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .sign(getAlgorithm());
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = JWT.require(getAlgorithm())
                .build()
                .verify(token);
        return jwt.getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            // e.g., TokenExpiredException, SignatureVerificationException
            return false;
        }
    }
}