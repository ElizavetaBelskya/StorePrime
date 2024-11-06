package ru.tinkoff.storePrime.models.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.tinkoff.storePrime.models.base.LongIdEntity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class Account extends LongIdEntity {

    public enum State {
        NOT_CONFIRMED, CONFIRMED, DELETED, BANNED

    }

    public enum Role {
        CUSTOMER, SELLER
    }

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String passwordHash;

    private Double cardBalance;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private State state;

    public boolean isConfirmed() {
        return !State.DELETED.equals(this.state);
    }

    public boolean isBanned() {
        return State.BANNED.equals(this.state) || State.DELETED.equals(this.state);
    }

}
