package ru.tinkoff.storePrime.models.chat;

import lombok.*;
import ru.tinkoff.storePrime.models.base.LongIdEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"senderId", "receiverId"})})
public class ChatMessage extends LongIdEntity {

    @ManyToOne
    private Chat chat;

    private Long senderId;

    private Long receiverId;

    private String text;

    private LocalDateTime sentAt;

    @ElementCollection
    private List<String> fileIds;


}
