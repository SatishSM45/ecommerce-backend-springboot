package org.example.ecommerce.service;

import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.dto.CartRequest;
import org.example.ecommerce.dto.CartResponse;

public interface CartService {
    CartResponse addToCart(CartRequest cartRequest);

    BaseResponse<CartResponse> getCartByCartId(Long cardId);

    BaseResponse<CartResponse> getCartsByUserEmail(int page, int size);

    void deleteCartById(Long cartId);

}
