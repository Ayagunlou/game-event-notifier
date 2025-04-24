package api.game_event_notifier.repository;

import api.game_event_notifier.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Role findByRoleId(Integer roleId);

    Role findByRoleName(String roleName);

    Role findFirstByOrderByRoleIdAsc();
}