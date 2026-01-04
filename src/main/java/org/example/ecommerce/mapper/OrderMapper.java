package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.OrderRes;
import org.example.ecommerce.dto.OrderResponse;
import org.example.ecommerce.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "createdAt", target = "orderDate")
    @Mapping(source = "status", target = "orderStatus")
    OrderRes entityToOrderRes(Order order);

    @Mapping(source = "createdAt", target = "orderDate")
    @Mapping(source = "status", target = "status")
    OrderResponse entityToOrderResponse(Order order);
}
