package kr.co.hanipaction.application.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/api/sse/order")
@RequiredArgsConstructor
public class SseController {
    private final SseService sseService;

    @GetMapping("/{storeId}")
    public SseEmitter subscribe(@PathVariable Long storeId) {
        return sseService.subscribe(storeId);
    }
}
