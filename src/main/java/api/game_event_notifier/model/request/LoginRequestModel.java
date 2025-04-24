package api.game_event_notifier.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginRequestModel {
    private String username;
    private String password;
    private String email;
}