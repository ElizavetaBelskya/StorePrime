package ru.tinkoff.storePrime.security.details;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.tinkoff.storePrime.models.user.Account;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

    private final Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleAuthority = account.getRole().name();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleAuthority);
        return Collections.singleton(authority);
    }

    @Override
    public String getPassword() {
        return account.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return account.isConfirmed();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !account.isBanned();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
