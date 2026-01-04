package org.example.ecommerce.util;

import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.exception.ValidationException;
import org.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommonService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderRepository orderRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ValidationException(1005, "user not found..!", "user not found..!"));
    }

    public Product findProductByProductId(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ValidationException(2001, "Product Not found..!", "Product Not found..!"));
    }

    public Product findByProductNameAndStatus(String productName, boolean status) {
        return productRepository.findByNameAndStatus(productName.toLowerCase(), status).orElseThrow(() -> new ValidationException(2001, "Product Not found..!", "Product Not found..!"));
    }

    public Optional<Cart> fetchProductWithUserCart(User user, Product product) {
        return cartRepository
                .findByUserAndProductAndStatus(user, product, true);
    }

    public Address findAddressByAddressId(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(() -> new ValidationException(4000, "Address not found..!", "Address not found..!"));
    }

    public Optional<Order> fetchOrderByOrderIdAndUser(String orderId, User user) {
        return orderRepository.findByOrderIdAndUser(orderId, user);
    }

    public Optional<Order> fetchOrderByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    public void roleAccessValidation(List<String> apiRoleAccess, List<String> rolesJwtToken) {
        boolean rolesMatched = apiRoleAccess.stream().anyMatch(res -> rolesJwtToken.contains(res));
        if (!rolesMatched) {
            throw new ValidationException(1013, "UnAuthorization for operation..!", "UnAuthorization for operation");
        }
    }
}
