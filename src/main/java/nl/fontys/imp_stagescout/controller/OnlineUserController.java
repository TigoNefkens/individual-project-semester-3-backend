package nl.fontys.imp_stagescout.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class OnlineUserController {

    private final SimpMessagingTemplate messagingTemplate;
    private final AtomicInteger onlineTabs = new AtomicInteger(0); // teller voor open tabs

    public OnlineUserController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/user/online")
    public void tabOpened() {
        onlineTabs.incrementAndGet();
        broadcastCount();
    }

    @MessageMapping("/user/offline")
    public void tabClosed() {
        onlineTabs.updateAndGet(count -> Math.max(0, count - 1));
        broadcastCount();
    }

    private void broadcastCount() {
        messagingTemplate.convertAndSend("/topic/online-count", onlineTabs.get());
    }
}
