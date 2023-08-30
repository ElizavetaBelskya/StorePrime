package ru.tinkoff.storePrime.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.dto.chat.ChatMessageDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaChatConsumer {

    private final ChatService chatService;

    private static final String CHAT_TOPIC = "chat-messages";

    @KafkaListener(topics = CHAT_TOPIC)
    public void receiveMessage(ChatMessageDto chatMessageDto) {
        log.info("Получено сообщение", chatMessageDto);
        chatService.saveMessage(chatMessageDto);
    }

}

