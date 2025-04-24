package api.game_event_notifier.service.auth;

import api.game_event_notifier.model.reponse.*;
import api.game_event_notifier.model.request.*;
import api.game_event_notifier.security.SecurityService;
import api.game_event_notifier.service.repository.ServiceRepository;
import api.game_event_notifier.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final SecurityService _securityService;
    private final ServiceRepository _serviceRepository;

    public AuthService(SecurityService securityService, ServiceRepository serviceRepository) {
        this._securityService = securityService;
        this._serviceRepository = serviceRepository;
    }

    public AuthResponseModel authenticate(LoginRequestModel request) {
        var user = _serviceRepository.getUser().findByUsername(request.getUsername());

        if (user == null){
            throw new RuntimeException("Invalid username or password");
        }

        boolean isMatch = _securityService.checkPassword(request.getPassword(), user.getPasswordHash());
        if (!isMatch) {
            throw new RuntimeException("Invalid username or password");
        }

        String accessToken = JwtUtil.generateAccessToken(user.getUserId().toString());
        String refreshToken = JwtUtil.generateRefreshToken(user.getUserId().toString());

        return new AuthResponseModel(user.getUsername(), accessToken, refreshToken);
    }

    public AuthResponseModel RefreshAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);  // ลบ 'Bearer ' ออก

            try {
                // ตรวจสอบว่า Access Token ที่ส่งมาถูกต้องและยังไม่หมดอายุ
                DecodedJWT decodedAccessToken = JwtUtil.verifyAccessToken(accessToken);
                String subject = decodedAccessToken.getSubject();

                // ถ้า Access Token ถูกต้อง, ก็ไม่จำเป็นต้องใช้ refresh token
                // สามารถสร้าง Access Token ใหม่หรือส่ง Access Token เดิมได้
                return new AuthResponseModel(subject, accessToken, null);
            } catch (Exception e) {
                // หาก Access Token ไม่ถูกต้องหรือหมดอายุ ให้ไปตรวจสอบ Refresh Token
                throw new RuntimeException("Invalid refreshToken");
            }
        }

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("refreshToken Expire");
        }

        try{

            if (JwtUtil.isRefreshTokenExpired(refreshToken)){
                throw new RuntimeException("Login Expire");
            }

            DecodedJWT decodedJWT = JwtUtil.verifyRefreshToken(refreshToken);
            String subject = decodedJWT.getSubject();

            // สร้าง Access Token ใหม่
            String newAccessToken = JwtUtil.generateAccessToken(subject);

            return new AuthResponseModel(subject, newAccessToken, null);
        }catch (Exception ex){
            throw new RuntimeException("Invalid refreshToken");
        }
    }
}
