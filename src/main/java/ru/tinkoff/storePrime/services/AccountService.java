package ru.tinkoff.storePrime.services;

import ru.tinkoff.storePrime.models.user.Account;

public interface AccountService {

    boolean isEmailUsed(String email);

    Account getUserByEmail(String email);

}
