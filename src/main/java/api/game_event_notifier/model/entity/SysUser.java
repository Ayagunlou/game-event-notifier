package api.game_event_notifier.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sys_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "salt", length = 255)
    private String salt;

    @Column(name = "create_date", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime updateDate;
}