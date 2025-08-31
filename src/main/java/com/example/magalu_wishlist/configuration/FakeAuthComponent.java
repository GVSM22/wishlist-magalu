package com.example.magalu_wishlist.configuration;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class FakeAuthComponent implements ExternalAuthComponent {

    public Optional<UserDetails> externalAuth(String bearerToken) {
        return Optional.of(
                new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return List.of(new SimpleGrantedAuthority("CUSTOMER"));
                    }

                    @Override
                    public @Nullable String getPassword() {
                        return "123";
                    }

                    @Override
                    public String getUsername() {
                        return bearerToken.replace("Bearer ", "") + "@provider.com";
                    }
                }
        );
    }

}
