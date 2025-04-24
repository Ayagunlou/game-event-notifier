package api.game_event_notifier.controller;

import api.game_event_notifier.model.entity.*;
import api.game_event_notifier.model.reponse.*;
import api.game_event_notifier.model.request.*;
import api.game_event_notifier.repository.UserRepository;
import api.game_event_notifier.service.auth.*;
import api.game_event_notifier.service.user.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class LoginController {

    private final CreateUserService _createUserService;
    private final AuthService _authServiceService;

    public LoginController(CreateUserService createUserService, AuthService authServiceService) {
        this._createUserService = createUserService;
        this._authServiceService = authServiceService;
    }

    @PostMapping("/login")
    public AuthResponseModel login(@RequestBody LoginRequestModel loginRequestModel, HttpServletRequest request, HttpServletResponse response) {

        var auth = _authServiceService.authenticate(loginRequestModel);

        String clientType = (String) request.getAttribute("clientType");
        if (clientType == null) clientType = "web";

        if ("mobile".equals(clientType)) {
            // ส่ง JSON ตรง (จะให้ส่ง JSON จริง ๆ หรือใช้ Map ก็ได้)
//            response.setContentType("application/json");
//            try {
//                String json = String.format("{\"access_token\":\"%s\",\"refresh_token\":\"%s\"}",
//                        auth.getAccessToken(), auth.getRefreshToken());
//                response.getWriter().write(json);
//            } catch (Exception e) {
//                throw new RuntimeException("Error writing response");
//            }
            return auth;

        } else {
            // Set เป็น HttpOnly cookie สำหรับ Web

            Cookie refreshCookie = new Cookie("refresh_token", auth.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(604800);

            response.addCookie(refreshCookie);

            var result = new AuthResponseModel();
            result.setUser(auth.getUser());
            result.setAccessToken(auth.getAccessToken());
            return result;
        }
    }

    @PostMapping("/create")
    public UserResponseModel createUser(@RequestBody LoginRequestModel loginRequestModel) {
        return _createUserService.createUser(loginRequestModel);
    }

    @PostMapping("/refresh-token")
    public AuthResponseModel refreshToken(HttpServletRequest request) {
        return _authServiceService.RefreshAccessToken(request);
    }
}