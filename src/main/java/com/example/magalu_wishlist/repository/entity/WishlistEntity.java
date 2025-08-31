package com.example.magalu_wishlist.repository.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document("wishlist")
@Data
@Builder
public class WishlistEntity {

    @Id
    private UUID id;

    private String email;

    @NotNull
    private List<ProductEntity> products;

}
