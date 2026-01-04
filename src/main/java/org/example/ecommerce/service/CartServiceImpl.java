package org.example.ecommerce.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.common.Result;
import org.example.ecommerce.config.JwtRequestContext;
import org.example.ecommerce.dto.CartRequest;
import org.example.ecommerce.dto.CartResponse;
import org.example.ecommerce.dto.PageResponse;
import org.example.ecommerce.entity.Cart;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.User;
import org.example.ecommerce.exception.ValidationException;
import org.example.ecommerce.mapper.CartMapper;
import org.example.ecommerce.repository.CartRepository;
import org.example.ecommerce.util.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private JwtRequestContext jwtRequestContext;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CartMapper cartMapper;


    public CartResponse addToCart(CartRequest cartRequest) {
        log.info("cartRequest: {} ", cartRequest);
        String email = jwtRequestContext.getEmail();
        User user = commonService.findByEmail(email);
        Product product = commonService.findProductByProductId(cartRequest.getProduct());
        Optional<Cart> cartOptional = commonService.fetchProductWithUserCart(user, product);
        if (ObjectUtils.isEmpty(cartOptional)) {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(cartRequest.getQuantity());
            cart.setStatus(true);
            cart.setPrice(BigDecimal.valueOf(product.getPrice()));
            Cart savedCart = cartRepository.save(cart);
            return cartMapper.entityToCartResponse(savedCart);
        } else {
            Cart existingProduct = cartOptional.get();
            existingProduct.setQuantity(existingProduct.getQuantity() + cartRequest.getQuantity());
            Cart existingCartSaved = cartRepository.save(existingProduct);
            return cartMapper.entityToCartResponse(existingCartSaved);
        }
    }

    @Override
    public BaseResponse<CartResponse> getCartByCartId(Long cartId) {
        Cart cart = findByCartIdAndStatus(cartId, true);
        //Validation for customer
        return BaseResponse.success(cartMapper.entityToCartResponse(cart));
    }

    public BaseResponse<CartResponse> getCartsByUserEmail(int page, int size) {
        String email = jwtRequestContext.getEmail();
        User user = commonService.findByEmail(email);
        Pageable pageable = PageRequest.of(page, size);
        Page<Cart> carts = cartRepository.findByUserCart(user, true, pageable);
        PageResponse<CartResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotalPages(carts.getTotalPages());
        pageResponse.setTotalElements(carts.getTotalElements());
        pageResponse.setData(carts.stream()
                .map(cart -> cartMapper.entityToCartResponse(cart)
                )
                .collect(Collectors.toList()));
        Result response = new Result();
        response.setSuccessCode(0);
        response.setSuccessDescription("Success");
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(pageResponse);
        baseResponse.setResult(response);
        return baseResponse;
    }

    //  delete cart by cartId
    @Transactional
    public void deleteCartById(Long cartId) {
        Cart cart = findByCartIdAndStatus(cartId, true);
        cart.setStatus(false);
        cartRepository.save(cart);
    }

    public Cart findByCartIdAndStatus(Long cartId, boolean staus) {
        return cartRepository.findByIdAndStatus(cartId, staus)
                .orElseThrow(() ->
                        new ValidationException(3001, "Cart not found", "Cart not found or already deleted"));
    }
}


