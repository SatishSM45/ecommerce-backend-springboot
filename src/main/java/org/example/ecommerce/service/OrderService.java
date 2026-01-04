package org.example.ecommerce.service;

import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.dto.*;
import org.example.ecommerce.entity.Order;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface OrderService {
    BaseResponse<OrderResponse> placeOrder(OrderRequest orderRequest);

    BaseResponse<PageResponse<OrderRes>> getOrdersByUserId(int page, int size);

    BaseResponse<OrderResponse> getOrdersById(String orderId);

    BaseResponse<OrderResponse> cancelOrder(String orderId);

    BaseResponse updateOrderStatus(String orderId, OrderUpdateStatusRequest orderUpdateStatusRequest);

    BaseResponse<OrderReturnResponse> orderReturn(String orderId, OrderReturnRequest orderRequest);
}
