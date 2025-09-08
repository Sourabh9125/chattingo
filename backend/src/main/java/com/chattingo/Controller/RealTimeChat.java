package com.chattingo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chattingo.Model.Message;

@Controller
public class RealTimeChat {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    public Message recieveMessage(@Payload Message message) {
        if (message.getChat().isGroup()) {
            simpMessagingTemplate.convertAndSend("/group/" + message.getChat().getId(), message);
        } else {
            simpMessagingTemplate.convertAndSendToUser(message.getChat().getId().toString(), "/private", message);
        }
        return message;
    }
}
