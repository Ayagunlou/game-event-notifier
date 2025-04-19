package api.game_event_notifier.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "key_pairs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyPair {

    @Id
    @Column(name = "key_id", nullable = false)
    private String keyId;

    @Column(name = "platform", nullable = false, length = 64)
    private String platform;

    @Column(name = "algorithm", nullable = false, length = 16)
    private String algorithm;

    @Column(name = "public_key", nullable = false, columnDefinition = "TEXT")
    private String publicKey;

    @Column(name = "private_key", nullable = false, columnDefinition = "TEXT")
    private String privateKey;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "expires_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime expiresAt;

    @OneToMany(mappedBy = "keyPair")
    private List<Token> tokens;
}

