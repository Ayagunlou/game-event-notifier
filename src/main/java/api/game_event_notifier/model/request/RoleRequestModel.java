package api.game_event_notifier.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RoleRequestModel {
    private String roleName;
    private String description;
}