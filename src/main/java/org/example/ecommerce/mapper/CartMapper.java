package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.CartResponse;
import org.example.ecommerce.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "cartId", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "quantity", source = "quantity")
    CartResponse entityToCartResponse(Cart cart);
}
