package com.example.magalu_wishlist.service;

import com.example.magalu_wishlist.controller.domain.ProductWishlist;
import com.example.magalu_wishlist.exceptions.MaxSizeWishlist;

import java.util.List;
import java.util.Optional;

public interface iWishlistService {
    List<ProductWishlist> listProducts(String email);
    Optional<ProductWishlist> findProduct(String email, String sku);
    ProductWishlist addProduct(String email, ProductWishlist productWishlist) throws MaxSizeWishlist;
    void removeProduct(String email, String sku);
}
