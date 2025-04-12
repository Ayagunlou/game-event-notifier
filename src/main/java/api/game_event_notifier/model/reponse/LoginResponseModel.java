package api.game_event_notifier.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseModel {
    private String user;
    private String token;
    private String message;
}