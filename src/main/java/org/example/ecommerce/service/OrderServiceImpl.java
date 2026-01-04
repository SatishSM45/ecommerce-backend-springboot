package org.example.ecommerce.service;

import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.common.CommonConstants;
import org.example.ecommerce.config.JwtRequestContext;
import org.example.ecommerce.dto.*;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.enums.OrderStatus;
import org.example.ecommerce.enums.PaymentStatus;
import org.example.ecommerce.exception.ValidationException;
import org.example.ecommerce.mapper.OrderItemMapper;
import org.example.ecommerce.mapper.OrderMapper;
import org.example.ecommerce.repository.OrderItemRepository;
import org.example.ecommerce.repository.OrderRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.util.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private JwtRequestContext jwtRequestContext;

    @Autowired
    private CommonService commonService;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Value("${order.update.status.roles}")
    private List<String> orderUpdateStatusRole;


    @Transactional
    public BaseResponse<OrderResponse> placeOrder(OrderRequest orderRequest) {
        String email = jwtRequestContext.getEmail();
        User user = commonService.findByEmail(email);
        //Validation correct customer and correct user
        Address address = commonService.findAddressByAddressId(orderRequest.getAddressId());
        //  Calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest item : orderRequest.getItems()) {
            Product product = commonService.findProductByProductId(item.getProductId());
            //checking before order ,the quantity of product is available in the ecommerce
            if (!(product.getStock() >= item.getQuantity())) {
                throw new ValidationException(7000, "Product: " + product.getName() + " Not Enough Quantity: " + item.getQuantity() + " To Create Order:", "Product: " + product.getName() + " Not enough Quantity:" + item.getQuantity() + " To Create Order:");
            }
            BigDecimal price = BigDecimal.valueOf(product.getPrice());
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            totalAmount = totalAmount.add(price.multiply(quantity));
        }
        //  Create Order
        Order order = new Order();
        //orderId=ECOM-ORD-YYYYMMDD-XXXXXX
        order.setOrderId(generateOrderId());
        order.setUser(user);
        order.setPaymentMode(orderRequest.getPaymentMode());
        order.setAddress(address);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentStatus(PaymentStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        //  Create Order Items
        List<OrderItemResponse> orderItemResponses = orderRequest.getItems().stream()
                .map(item -> {
                    Product product = commonService.findProductByProductId(item.getProductId());
                    BigDecimal price = BigDecimal.valueOf(product.getPrice());
                    BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                    BigDecimal itemTotal = price.multiply(quantity);

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(price);
                    orderItem.setTotalPrice(itemTotal);

                    orderItemRepository.save(orderItem);
                    product.setStock(product.getStock() - item.getQuantity());
                    productRepository.save(product);
                    OrderItemResponse response = new OrderItemResponse();
                    response.setProductId(product.getId());
                    response.setProductName(product.getName());
                    response.setQuantity(item.getQuantity());
                    response.setPrice(price);
                    response.setTotalPrice(itemTotal);

                    return response;
                })
                .collect(Collectors.toList());
        //  Prepare Response
        //
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(savedOrder.getOrderId());
        orderResponse.setStatus(savedOrder.getStatus());
        orderResponse.setTotalAmount(savedOrder.getTotalAmount());
        orderResponse.setPaymentStatus(savedOrder.getPaymentStatus());
        orderResponse.setOrderDate(savedOrder.getCreatedAt());
        orderResponse.setItems(orderItemResponses);
        return BaseResponse.success(orderResponse);
    }


    @Override
    public BaseResponse<PageResponse<OrderRes>> getOrdersByUserId(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String email = jwtRequestContext.getEmail();
        User user = commonService.findByEmail(email);
        log.info("getOrderBy email: {} ,", email);
        Page<Order> ordersPage = orderRepository.findByUser_Id(user.getId(), pageable);
        PageResponse<OrderRes> orderResPageResponse = new PageResponse<>();
        log.info("responseOrders:   {}", ordersPage.getContent().stream().map(orderMapper::entityToOrderRes).toList());
        orderResPageResponse.setData(ordersPage.getContent().stream().map(orderMapper::entityToOrderRes).toList());
        orderResPageResponse.setTotalPages(ordersPage.getTotalPages());
        orderResPageResponse.setTotalElements(ordersPage.getTotalElements());
        return BaseResponse.success(orderResPageResponse);
    }

    @Override
    public BaseResponse<OrderResponse> getOrdersById(String orderId) {
        String email = jwtRequestContext.getEmail();
        User user = commonService.findByEmail(email);
        Order order = commonService.fetchOrderByOrderIdAndUser(orderId, user).orElseThrow(() -> new ValidationException(5010, "Order Not Found ", "Order Not Found "));
        if (order.getOrderItems() == null || order.getOrderItems().size() == 0) {
            throw new ValidationException(5010, "Order Item Not Found ", "Order Item Not FOUND");
        }
        log.info("order: {} ", order);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setPaymentStatus(order.getPaymentStatus());
        orderResponse.setPaymentMode(order.getPaymentMode());
        orderResponse.setOrderDate(order.getCreatedAt());
        orderResponse.setItems(order.getOrderItems().stream().map(orderItemMapper::entityToResponse).toList());
        return BaseResponse.success(orderResponse);
    }

    @Override
    public BaseResponse<OrderResponse> cancelOrder(String orderId) {
        String email = jwtRequestContext.getEmail();
        User user = commonService.findByEmail(email);
        Order order = orderRepository.findByOrderIdAndUser(orderId, user).orElseThrow(() -> new ValidationException(5010, "Order Not Found ", "Order Not Found "));
        //Need to call the refund api's
        if (OrderStatus.CANCELLED == order.getStatus()) {
            throw new ValidationException(5011, "Order Id already cancelled", "Order Id already cancelled");
        }
        if (order.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new ValidationException(5012, " Payment Pending ", "Payment Pending");
        }
//        order.setPaymentStatus(PaymentStatus.REFUNDED);//real time
        order.setStatus(OrderStatus.CANCELLED);
        //PaymentAPI refund
        Order cancelOrder = orderRepository.save(order);
        return BaseResponse.success(orderMapper.entityToOrderResponse(cancelOrder));
    }

    @Override
    public BaseResponse updateOrderStatus(String orderId, OrderUpdateStatusRequest orderUpdateStatusRequest) {
        List<String> jwtRoles = jwtRequestContext.getRoles();
        commonService.roleAccessValidation(orderUpdateStatusRole, jwtRoles);
        Optional<Order> orderOptional = commonService.fetchOrderByOrderId(orderId);
        if (orderOptional.isEmpty()) {
            throw new ValidationException(5010, "Order Not Found ", "Order Not Found ");
        }
        OrderStatus requestedStatus;
        try {
            requestedStatus = OrderStatus.valueOf(orderUpdateStatusRequest.getStatus());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(8000, "Invalid order status", "Invalid order status");
        }
        Order order = orderOptional.get();
        if (requestedStatus == (OrderStatus.DELIVERED)) {
            if (order.getStatus() == OrderStatus.DELIVERED) {
                throw new ValidationException(8001, "Order Already delivered", "Order Already delivered");
            }
            if (order.getStatus() != OrderStatus.SHIPPED || order.getPaymentStatus() != PaymentStatus.SUCCESS) {
                throw new ValidationException(8001, "Order not At delivered state", "Order not At delivered state");
            }
        } else if (requestedStatus == (OrderStatus.SHIPPED)) {
            if (order.getStatus() != OrderStatus.CREATED) {
                throw new ValidationException(8002, "Order Not At Created State", "Order Not At Created State");
            }
        } else if (requestedStatus == (OrderStatus.RETURNED)) {
            if (order.getPaymentStatus() != PaymentStatus.SUCCESS || order.getStatus() != (OrderStatus.RETURN_REQUESTED)) {
                throw new ValidationException(8004, "Order Not requested for return ", "Order Not requested for return ");
            }
        } else {
            throw new ValidationException(8005, "order state mismatch", "order state mismatch");
        }
        order.setStatus(OrderStatus.valueOf(orderUpdateStatusRequest.getStatus()));
        orderRepository.save(order);
        return BaseResponse.success(null);
    }

    @Override
    public BaseResponse<OrderReturnResponse> orderReturn(String orderId, OrderReturnRequest orderRequest) {
        String email = jwtRequestContext.getEmail();
        User user = commonService.findByEmail(email);
        Optional<Order> orderOptional = commonService.fetchOrderByOrderIdAndUser(orderId, user);
        Order order = null;
        if (orderOptional.isEmpty()) {
            throw new ValidationException(5010, "Order Not Found ", "Order Not Found ");
        }
        order = orderOptional.get();
        if (order.getPaymentStatus() == PaymentStatus.SUCCESS && order.getStatus() == OrderStatus.DELIVERED) {
            order.setStatus(OrderStatus.RETURN_REQUESTED);
            order.setReason(orderRequest.getReason());
            orderRepository.save(order);
        } else {
            throw new ValidationException(8087, "Can Not Allowed To Return Now", "Can Not Allowed To Return Now");
        }
        OrderReturnResponse orderReturnResponse = new OrderReturnResponse();
        orderReturnResponse.setOrderId(orderId);
        orderReturnResponse.setOrderStatus(OrderStatus.RETURN_REQUESTED);
        orderReturnResponse.setReason(orderRequest.getReason());
        return BaseResponse.success(orderReturnResponse);
    }

    public String generateOrderId() {
        //year-month -date
        String date = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String randomUuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return CommonConstants.ORDER_ID_INITIAL + CommonConstants.HYPEN + date + CommonConstants.HYPEN + randomUuid;
    }
}
