package ru.tinkoff.storePrime.models.user;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode
@ToString
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

    @Column()
    private String phoneNumber;

    @Column(nullable = false)
    private String passwordHash;

    private Double cardBalance;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private State state;

    public boolean isConfirmed() {
        return State.CONFIRMED.equals(this.state);
    }

    public boolean isBanned() {
        return State.BANNED.equals(this.state);
    }

}
