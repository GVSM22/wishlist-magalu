package com.example.magalu_wishlist.exceptions;

public class MaxSizeWishlist extends Exception {

    public MaxSizeWishlist(int size) {
        super("Wishlist can't have more than " + size + " items");
    }

}
