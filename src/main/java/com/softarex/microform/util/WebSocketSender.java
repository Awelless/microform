package com.softarex.microform.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softarex.microform.domain.Response;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WebSocketSender {
    private final ObjectMapper mapper;
    private final SimpMessagingTemplate template;

    /**
     * Converts and sends response to /topic/responses
     */
    public void sendResponse(@NonNull final Response response) throws JsonProcessingException {
        String value = mapper.writeValueAsString(response);
        template.convertAndSend("/topic/responses", value);
    }
}
