package api.game_event_notifier.config;

import api.game_event_notifier.model.entity.ErrorLog;
import api.game_event_notifier.repository.ErrorLogRepository;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.time.LocalDateTime;

public class ErrorLogDbAppender extends AppenderBase<ILoggingEvent> {

    @Setter
    private static ApplicationContext applicationContext;

    @Override
    public void append(ILoggingEvent event) {
        if (!event.getLevel().toString().equals("ERROR")) return;

        try {
            ErrorLogRepository repo = applicationContext.getBean(ErrorLogRepository.class);

            ErrorLog log = new ErrorLog();
            log.setLevel(event.getLevel().toString());
            log.setLogger(event.getLoggerName());
            log.setMessage(event.getFormattedMessage());
            log.setThread(event.getThreadName());
            log.setTimestamp(LocalDateTime.now());

            if (event.getThrowableProxy() != null) {
                log.setException(event.getThrowableProxy().getMessage());
            }

            repo.save(log);
        } catch (Exception e) {
            System.err.println("Failed to write log to DB: " + e.getMessage());
        }
    }
}

