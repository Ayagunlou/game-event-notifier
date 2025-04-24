package api.game_event_notifier.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@ControllerAdvice  // ใช้เพื่อจัดการ exception ทั่วทั้งแอปพลิเคชัน
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequestException(BadRequestException ex) {
        return "BAD_REQUEST " + ex.getMessage();
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
//        return new ErrorResponse("FORBIDDEN", ex.getMessage());
//    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException ex) {
        return ex.getMessage() + "Something went wrong, please try again later.";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleRuntimeException(Exception ex) {
        return "UNAUTHORIZED.";
    }
}
