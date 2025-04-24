package api.game_event_notifier.model.reponse;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseModel {
    private String user;
    private String accessToken;
    private String refreshToken;
}