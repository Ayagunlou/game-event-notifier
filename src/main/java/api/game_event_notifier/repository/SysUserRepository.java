package api.game_event_notifier.repository;

import api.game_event_notifier.model.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser,Integer> {

    @Query("SELECT s FROM SysUser s WHERE s.username = :username")
    SysUser findByUsername(@Param("username") String username);

    List<SysUser> findByCreateDate(LocalDateTime createDate);

    List<SysUser> findByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}