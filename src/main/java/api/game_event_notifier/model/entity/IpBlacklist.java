package api.game_event_notifier.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ip_blacklist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ipId;

    @Column(nullable = false)
    private String ipAddress;

    private String reason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
