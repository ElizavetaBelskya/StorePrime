package ru.tinkoff.storePrime.security.details;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.tinkoff.storePrime.exceptions.not_found.NotFoundException;
import ru.tinkoff.storePrime.services.AccountService;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return new UserDetailsImpl(accountService.getUserByEmail(email));
        } catch (NotFoundException ex) {
            throw new UsernameNotFoundException(ex.getMessage());
        }
    }

}
