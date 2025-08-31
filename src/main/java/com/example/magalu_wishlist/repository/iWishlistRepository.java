package com.example.magalu_wishlist.repository;

import com.example.magalu_wishlist.controller.domain.ProductWishlist;

import java.util.List;
import java.util.Optional;

public interface iWishlistRepository {

    List<ProductWishlist> listProducts(String email);
    int wishlistSize(String email);
    boolean hasProduct(String email, String sku);
    Optional<ProductWishlist> findProduct(String email, String sku);
    ProductWishlist addProduct(String email, ProductWishlist productWishlist);
    void removeProduct(String email, String sku);

}
