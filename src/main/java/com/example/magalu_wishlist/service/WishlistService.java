package com.example.magalu_wishlist.service;

import com.example.magalu_wishlist.controller.domain.ProductWishlist;
import com.example.magalu_wishlist.exceptions.MaxSizeWishlist;
import com.example.magalu_wishlist.repository.iWishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService implements iWishlistService {

    private final int MAX_SIZE;

    public WishlistService(@Value("${wishlist.maxSize}") int size) {
        MAX_SIZE = size;
    }

    @Autowired
    private iWishlistRepository repository;

    @Override
    public List<ProductWishlist> listProducts(String email) {
        return repository.listProducts(email);
    }
    @Override
    public Optional<ProductWishlist> findProduct(String email, String sku) {
        return repository.findProduct(email, sku);
    }
    @Override
    public ProductWishlist addProduct(String email, ProductWishlist productWishlist) throws MaxSizeWishlist {
        if (repository.hasProduct(email, productWishlist.sku()))
            return productWishlist;

        if (repository.wishlistSize(email) >= MAX_SIZE)
            throw new MaxSizeWishlist(MAX_SIZE);

        return repository.addProduct(email, productWishlist);
    }

    @Override
    public void removeProduct(String email, String sku) {
        repository.removeProduct(email, sku);
    }

}
