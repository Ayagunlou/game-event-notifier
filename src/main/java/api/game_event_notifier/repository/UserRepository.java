package api.game_event_notifier.repository;

import api.game_event_notifier.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("SELECT s FROM User s WHERE s.username = :username")
    User findByUsername(@Param("username") String username);

    List<User> findByCreatedAt(LocalDateTime createDate);

    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}