package api.game_event_notifier.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    static JwtConfig jwtConfig = new JwtConfig();
    private static final Algorithm accessTokenAlgorithm = Algorithm.HMAC256(jwtConfig.getSecret());
    private static final Algorithm refreshTokenAlgorithm = Algorithm.HMAC256(jwtConfig.getSecretR());
//    private static final Algorithm refreshTokenAlgorithm = Algorithm.RSA256(jwtConfig.getPublicKey(), jwtConfig.getPrivateKey());

    private static final long expireTimeAccess  = 15 * 60 * 1000; // 15 นาที
    private static final long expireTimeRefresh  = 7 * 24 * 60 * 60 * 1000; // 7 วัน

    // สำหรับ AccessToken
    public static String generateAccessToken(String subject) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTimeAccess))
                .sign(accessTokenAlgorithm);  // ใช้ HMAC256 สำหรับ AccessToken
    }


    // สำหรับ RefreshToken
    public static String generateRefreshToken(String subject) {
        return JWT.create()
                .withSubject(subject)
                .withClaim("origin", "Device")  // ข้อมูลเกี่ยวกับแพลตฟอร์มหรืออุปกรณ์
                .withClaim("jti", UUID.randomUUID().toString())  // ใช้ jti เพื่อระบุ token ที่ไม่ซ้ำ
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTimeRefresh))
                .sign(refreshTokenAlgorithm);  // ใช้ RSA256 สำหรับ RefreshToken
    }

    // สำหรับการ Verify AccessToken
    public static DecodedJWT verifyAccessToken(String token) {
        JWTVerifier verifier = JWT.require(accessTokenAlgorithm).build();
        return verifier.verify(token); // ตรวจสอบ AccessToken ด้วย HMAC256
    }

    // สำหรับการ Verify RefreshToken
    public static DecodedJWT verifyRefreshToken(String token) {
        JWTVerifier verifier = JWT.require(refreshTokenAlgorithm).build();
        return verifier.verify(token); // ตรวจสอบ RefreshToken ด้วย RSA256
    }

    public static boolean isRefreshTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = verifyRefreshToken(token);
            Date expiration = decodedJWT.getExpiresAt();
            return expiration.before(new Date()); // ถ้าวันหมดอายุก่อนเวลาปัจจุบัน แสดงว่าหมดอายุ
        } catch (Exception e) {
            // ถ้าเกิดข้อผิดพลาด เช่น token ไม่ถูกต้อง หรือ verify ล้มเหลว ให้ถือว่าหมดอายุ
            return true;
        }
    }
}