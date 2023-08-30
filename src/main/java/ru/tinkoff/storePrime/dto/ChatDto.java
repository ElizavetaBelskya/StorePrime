package ru.tinkoff.storePrime.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.dto.chat.ChatMessageDto;
import ru.tinkoff.storePrime.dto.user.AccountDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Schema(description = "Чат")
public class ChatDto {

    private AccountDto interlocutorDto;

    private List<ChatMessageDto> chatMessageDto;

}
