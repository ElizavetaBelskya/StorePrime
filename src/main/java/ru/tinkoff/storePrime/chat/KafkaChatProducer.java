package ru.tinkoff.storePrime.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.dto.chat.ChatMessageDto;

@Service
public class KafkaChatProducer {

    private final KafkaTemplate<String, ChatMessageDto> kafkaTemplate;

    private static final String CHAT_TOPIC = "chat-messages";

    @Autowired
    public KafkaChatProducer(KafkaTemplate<String, ChatMessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(ChatMessageDto messageDto) {
        kafkaTemplate.send(CHAT_TOPIC, messageDto);
    }

}

