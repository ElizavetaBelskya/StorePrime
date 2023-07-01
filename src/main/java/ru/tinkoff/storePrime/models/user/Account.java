package ru.tinkoff.storePrime.models.user;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class Account {

    public enum State {
        NOT_CONFIRMED, CONFIRMED, DELETED, BANNED
    }

    public enum Role {
        CUSTOMER, SELLER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String passwordHash;

    private Long cardBalance;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private State state;

    public boolean isConfirmed() {
        return this.state.equals(State.CONFIRMED);
    }

    public boolean isBanned() {
        return this.state.equals(State.BANNED);
    }

}
