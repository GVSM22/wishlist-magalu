package com.example.magalu_wishlist.repository;

import com.example.magalu_wishlist.controller.domain.ProductWishlist;
import com.example.magalu_wishlist.repository.entity.ProductEntity;
import com.example.magalu_wishlist.repository.entity.WishlistEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class WishlistRepository implements iWishlistRepository {

    @Autowired
    private MongoTemplate template;

    @Override
    public List<ProductWishlist> listProducts(String email) {
        return findByEmail(email)
                .map(entity ->
                        entity
                                .getProducts()
                                .stream()
                                .map(it -> new ProductWishlist(it.getSku()))
                                .toList()
                )
                .orElseGet(List::of);
    }

    @Override
    public int wishlistSize(String email) {
        return findByEmail(email)
                .map(it -> it.getProducts().size())
                .orElse(0);
    }

    @Override
    public boolean hasProduct(String email, String sku) {
        return template
                .query(WishlistEntity.class)
                .inCollection("wishlist")
                .matching(
                        query(where("email").is(email))
                                .addCriteria(where("products").is(sku))
                )
                .exists();
    }

    @Override
    public Optional<ProductWishlist> findProduct(String email, String sku) { // TODO maybe there is a native solution to this
        return findByEmailAndSku(email, sku)
                .flatMap(it ->
                        it.getProducts()
                                .stream()
                                .filter(p -> p.getSku().equalsIgnoreCase(sku))
                                .findFirst()
                                .map(p -> new ProductWishlist(p.getSku()))
                );
    }

    @Override
    public ProductWishlist addProduct(String email, ProductWishlist productWishlist) {
        return findByEmail(email)
                .map(entity -> {
                    entity.getProducts().add(ProductEntity.builder().sku(productWishlist.sku()).build());
                    var res = template.updateFirst(
                            Query.query(Criteria.where("_id").is(entity.getId())),
                            new Update().set("products", entity.getProducts()),
                            WishlistEntity.class
                    );
                    return productWishlist;
                })
                .orElseGet(() -> {
                    var entity = WishlistEntity.builder()
                            .id(UUID.randomUUID())
                            .email(email)
                            .products(List.of(ProductEntity.builder().sku(productWishlist.sku()).build()))
                            .build();

                    template.insert(entity, "wishlist");
                    return productWishlist;
                });
    }

    @Override
    public void removeProduct(String email, String sku) {
        findByEmailAndSku(email, sku)
                .ifPresent(wishlistEntity -> {
                    var newList = wishlistEntity.getProducts()
                            .stream().filter(it -> !it.getSku().equalsIgnoreCase(sku))
                            .toList();

                    template.updateFirst(
                            Query.query(where("_id").is(wishlistEntity.getId())),
                            new Update().set("products", newList),
                            WishlistEntity.class
                    );
                });
    }

    private Optional<WishlistEntity> findByEmail(String email) {
        return template.query(WishlistEntity.class)
                .inCollection("wishlist")
                .matching(query(where("email").is(email)))
                .one();
    }

    private Optional<WishlistEntity> findByEmailAndSku(String email, String sku) {
        return         template
                .query(WishlistEntity.class)
                .inCollection("wishlist")
                .matching(
                        query(where("email").is(email))
                        .addCriteria(where("products").is(sku))
                )
                .one();
    }
}
