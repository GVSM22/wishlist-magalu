package com.example.magalu_wishlist.controller.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ProductWishlist(
        @NotNull(message = "sku can't be null")
        @NotBlank (message = "sku can't be empty")
        String sku
){}
