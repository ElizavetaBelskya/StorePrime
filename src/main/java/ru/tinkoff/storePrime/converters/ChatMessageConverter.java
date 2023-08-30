package ru.tinkoff.storePrime.converters;

import ru.tinkoff.storePrime.dto.chat.ChatMessageDto;
import ru.tinkoff.storePrime.models.chat.ChatMessage;

public class ChatMessageConverter {

    private ChatMessageConverter(){}

    public static ChatMessage getChatMessageFromChatMessageDto(ChatMessageDto chatMessageDto) {
        return ChatMessage.builder()
                .senderId(chatMessageDto.getSenderId())
                .receiverId(chatMessageDto.getReceiverId())
                .text(chatMessageDto.getText())
                .sentAt(chatMessageDto.getSentAt())
                .fileIds(chatMessageDto.getFileIds())
                .build();
    }

}
