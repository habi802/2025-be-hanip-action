package kr.co.hanipaction.application.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long storeId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(storeId, emitter);

        emitter.onCompletion(() -> emitters.remove(storeId));
        emitter.onTimeout(() -> emitters.remove(storeId));

        try {
            // 연결 직후 성공 메시지 전송
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("SSE 연결 성공: storeId=" + storeId));
        } catch (IOException e) {
            emitter.completeWithError(e);
            emitters.remove(storeId);
        }

        return emitter;
    }

    // 주문 이벤트 발생시 호출
    public void sendOrder(Long storeId, Object data) {
        SseEmitter emitter = emitters.get(storeId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("order")
                        .data(data));
            } catch (IOException e) {
                emitters.remove(storeId);
            }
        }
    }
}

