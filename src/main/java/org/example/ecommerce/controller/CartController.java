package org.example.ecommerce.controller;

import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.dto.CartRequest;
import org.example.ecommerce.dto.CartResponse;
import org.example.ecommerce.service.CartService;
import org.example.ecommerce.service.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public CartResponse addToCart(@RequestBody CartRequest cartRequest) {
        return cartService.addToCart(cartRequest);
    }

    //  Get all carts by userEmail
    @GetMapping("/user")
    public ResponseEntity<BaseResponse<CartResponse>> getCartsByUserEmail(
            @RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "5", name = "size") int size
    ) {
        return ResponseEntity.ok(cartService.getCartsByUserEmail(page, size));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<BaseResponse<CartResponse>> getCartsByCartId(@PathVariable("cartId") Long cartId) {
        return ResponseEntity.ok(cartService.getCartByCartId(cartId));
    }

    //  Delete cart by cartId
    @DeleteMapping("/{cartId}")
    public ResponseEntity<BaseResponse> deleteCartById(
            @PathVariable(name = "cartId") Long cartId
    ) {
        cartService.deleteCartById(cartId);
        return ResponseEntity.ok(BaseResponse.success(null));
    }
}
