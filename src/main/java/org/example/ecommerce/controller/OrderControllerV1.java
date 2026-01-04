package org.example.ecommerce.controller;


import jakarta.validation.Valid;
import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.dto.OrderUpdateStatusRequest;
import org.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/admin/orders")
public class OrderControllerV1 {
    @Autowired
    private OrderService orderService;

    //Update Order Status (ADMIN / DELIVERY AGENT)
    @PutMapping("/{orderId}/status")
    public ResponseEntity<BaseResponse> updateOrderStatus(@PathVariable("orderId") String orderId, @RequestBody @Valid OrderUpdateStatusRequest orderUpdateStatusRequest) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderUpdateStatusRequest));
    }


}
