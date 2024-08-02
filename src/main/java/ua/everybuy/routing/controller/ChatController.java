package ua.everybuy.routing.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class ChatController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    @CrossOrigin("*")
    public String sendMessage(@Payload ChatMessage message) {
        return "Hello " + message.getContent();
    }
}
@Getter
@Setter
class ChatMessage {
    private String content;

}