package ru.tinkoff.storePrime.models.chat;

import lombok.*;
import ru.tinkoff.storePrime.models.base.LongIdEntity;
import ru.tinkoff.storePrime.models.user.Customer;
import ru.tinkoff.storePrime.models.user.Seller;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table
public class Chat extends LongIdEntity {

    @OneToMany
    private List<ChatMessage> messageList;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Seller seller;


}
