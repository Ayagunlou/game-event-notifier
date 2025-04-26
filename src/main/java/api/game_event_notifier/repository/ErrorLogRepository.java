package api.game_event_notifier.repository;

import api.game_event_notifier.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
}
