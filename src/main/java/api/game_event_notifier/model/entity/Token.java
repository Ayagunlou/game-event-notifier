package api.game_event_notifier.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", nullable = false)
    private Integer tokenId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "refresh_token", nullable = false, length = 512)
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "key_id")
    private KeyPair keyPair;

    @Column(name = "platform", length = 64)
    private String platform;

    @Column(name = "device_id", length = 128)
    private String deviceId;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime expiresAt;
}

