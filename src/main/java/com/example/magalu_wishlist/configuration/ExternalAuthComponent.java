package com.example.magalu_wishlist.configuration;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface ExternalAuthComponent {

    Optional<UserDetails> externalAuth(String bearerToken);

}
