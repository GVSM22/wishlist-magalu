package com.example.magalu_wishlist.service;

import com.example.magalu_wishlist.controller.domain.ProductWishlist;
import com.example.magalu_wishlist.exceptions.MaxSizeWishlist;
import com.example.magalu_wishlist.repository.iWishlistRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WishlistServiceTest {

    @Mock
    private iWishlistRepository repository;

    @InjectMocks
    private WishlistService service = new WishlistService(1);

    @Test
    public void givenNoItemsOnWishlistShouldReturnEmptyList() {
        List<ProductWishlist> items = List.of();

        when(repository.listProducts(any())).thenReturn(items);

        var products = service.listProducts("email");

        assertThat(products.size()).isEqualTo(0);
    }

    @Test
    public void givenSomeItemsOnWishlistShouldReturnThoseItems() {
        var items = List.of(new ProductWishlist("sku1"), new ProductWishlist("sku2"));

        when(repository.listProducts(any())).thenReturn(items);

        var products = service.listProducts("email");

        products.forEach(item -> {
            assertThat(items.stream().anyMatch(p -> p.sku().equalsIgnoreCase(item.sku()))).isTrue();
        });
    }

    @Test
    public void givenNoProductFoundShouldReturnOptionEmpty() {
        Optional<ProductWishlist> item = Optional.empty();

        when(repository.findProduct(any(), any())).thenReturn(item);

        var product = service.findProduct("email", "sku1");

        assertThat(product.isEmpty()).isTrue();
    }

    @Test
    public void givenWishlistShouldFindSpecificProduct() {
        var item = Optional.of(new ProductWishlist("sku1"));

        when(repository.findProduct(any(), any())).thenReturn(item);

        var product = service.findProduct("email", "sku1");

        product.map(it -> assertThat(it.sku()).isEqualTo(item.get().sku()))
                .orElseThrow();
    }

    @Test
    public void givenProductAlreadyOnWishlistWhenTryingToAddItShouldDoNothing() throws MaxSizeWishlist {
        var product = new ProductWishlist("sku1");

        when(repository.hasProduct(any(), any())).thenReturn(true);

        var result = service.addProduct("email", product);

        assertThat(result.sku()).isEqualTo(product.sku());
    }

    @Test
    public void givenFullWishlistWhenTryToAddShouldThrowException() {
        var product = new ProductWishlist("sku1");

        when(repository.hasProduct(any(), any())).thenReturn(false);
        when(repository.wishlistSize(any())).thenReturn(1);


        assertThrows(MaxSizeWishlist.class, () -> service.addProduct("email", product));
    }

    @Test
    public void shouldBeAbleToAddItemToWishlist() throws MaxSizeWishlist {
        var product = new ProductWishlist("sku1");

        when(repository.hasProduct(any(), any())).thenReturn(false);
        when(repository.wishlistSize(any())).thenReturn(0);
        when(repository.addProduct(any(), any())).thenReturn(product);

        var result = service.addProduct("email", product);

        assertThat(product.sku()).isEqualTo(result.sku());
    }

}
