package com.example.magalu_wishlist.controller;

import com.example.magalu_wishlist.controller.domain.ProductWishlist;
import com.example.magalu_wishlist.exceptions.MaxSizeWishlist;
import com.example.magalu_wishlist.service.iWishlistService;
import com.example.magalu_wishlist.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private iWishlistService wishlistService;

    @GetMapping
    public ResponseEntity<List<ProductWishlist>> listProducts(HttpServletRequest request) {
        return ResponseEntity.ok(
                wishlistService.listProducts(RequestUtils.getEmail(request))
        );
    }

    @GetMapping("/product/{sku}")
    public ResponseEntity<ProductWishlist> findProduct(HttpServletRequest request, @PathVariable String sku) {
        return wishlistService.findProduct(RequestUtils.getEmail(request), sku)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }

    @PutMapping
    public ResponseEntity<ProductWishlist> addProduct(HttpServletRequest request, @RequestBody @Validated ProductWishlist productWishlist) {
        try {
            final var product = wishlistService.addProduct(RequestUtils.getEmail(request), productWishlist);
            return ResponseEntity.ok(product);
        } catch (MaxSizeWishlist ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/product/{sku}")
    public void removeProduct(HttpServletRequest request, @PathVariable String sku) {
        wishlistService.removeProduct(
                RequestUtils.getEmail(request),
                sku
        );
    }

}
