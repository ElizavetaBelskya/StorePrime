package ru.tinkoff.storePrime.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Schema(description = "Сообщение в чате")
public class ChatMessageDto {

    @Schema(description = "Идентификатор отправителя", example = "13")
    private Long senderId;

    @Schema(description = "Идентификатор получателя", example = "3")
    private Long receiverId;

    @Schema(description = "Текст сообщения", example = "Новое сообщение")
    private String text;

    @Schema(description = "Время отправки сообщения", example = "2023-07-20T14:30:00")
    private LocalDateTime sentAt;

    @Schema(description = "Идентификаторы иллюстраций")
    private List<String> fileIds;

    @Schema(description = "Роль отправителя")
    private String senderRole;



}
