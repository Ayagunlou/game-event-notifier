package api.game_event_notifier.controller;

import api.game_event_notifier.model.entity.SysUser;
import api.game_event_notifier.model.reponse.LoginResponseModel;
import api.game_event_notifier.model.request.LoginRequestModel;
import api.game_event_notifier.service.user.CreateUserService;
import api.game_event_notifier.service.user.LoginUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LoginController {

    private final CreateUserService _createUserService;
    private final LoginUserService _loginUserService;

    public LoginController(CreateUserService createUserService, LoginUserService loginUserService) {
        this._createUserService = createUserService;
        this._loginUserService = loginUserService;
    }

    @PostMapping("/login")
    public LoginResponseModel login(@RequestBody LoginRequestModel loginRequestModel) {
        return _loginUserService.login(loginRequestModel);
    }

    @PostMapping("/create")
    public SysUser createUser(@RequestBody LoginRequestModel loginRequestModel) {
        return _createUserService.createUser(loginRequestModel);
    }

    @PostMapping("/test")
    public String createUser() {
        return "Hello";
    }
}