package api.game_event_notifier.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "error_logs")
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String level;
    private String logger;
    private String message;
    private String thread;
    private String exception;

    private LocalDateTime timestamp;

    // getters/setters
}
