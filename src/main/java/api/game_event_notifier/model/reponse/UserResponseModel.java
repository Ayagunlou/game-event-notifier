package api.game_event_notifier.model.reponse;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseModel {
    private String user;
    private String email;
}