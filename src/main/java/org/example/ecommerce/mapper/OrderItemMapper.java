package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.OrderItemResponse;
import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source ="product.name", target ="productName")
    @Mapping(source ="product.id", target ="productId")
    OrderItemResponse entityToResponse(OrderItem orderItem);
}
