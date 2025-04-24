package api.game_event_notifier.controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @PostMapping("/hello")
    public String test() {
        return "Hello";
    }
}