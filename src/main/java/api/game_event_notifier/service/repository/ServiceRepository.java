package api.game_event_notifier.service.repository;

import lombok.*;
import org.springframework.stereotype.Component;
import api.game_event_notifier.repository.*;

@Component
@Getter
@RequiredArgsConstructor
public class ServiceRepository {
    private final UserRepository user;
    private final RoleRepository role;
}
