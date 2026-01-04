package org.example.ecommerce.service;


import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.common.Result;
import org.example.ecommerce.config.JwtRequestContext;
import org.example.ecommerce.dto.PageResponse;
import org.example.ecommerce.dto.ProductRequest;
import org.example.ecommerce.dto.ProductResponse;
import org.example.ecommerce.entity.Category;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.exception.ValidationException;
import org.example.ecommerce.mapper.ProductMapper;
import org.example.ecommerce.repository.CategoryRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.util.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private JwtRequestContext jwtRequestContext;
    @Autowired
    private CommonService commonService;
    @Value("${add.product.roles}")
    private List<String> addProductRoles;
    @Value("${update.product.roles}")
    private List<String> updateProductRoles;


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BaseResponse<ProductResponse> addProduct(ProductRequest productRequest) {
        List<String> jwtRoles = jwtRequestContext.getRoles();
        commonService.roleAccessValidation(addProductRoles, jwtRoles);
        productRequest.setName(productRequest.getName().toLowerCase());
        log.info("Product name:: {} ", productRequest.getName());
        Optional<Product> productOptional = productRepository.findByName(productRequest.getName().toLowerCase());
        log.error("Product  :{} ", productOptional);
        if (productOptional.isPresent()) {
            Product existProduct = productOptional.get();
            existProduct.setStock(existProduct.getStock() + productRequest.getStock());
            Product existProductSaved = productRepository.save(existProduct);
            ProductResponse productResponse = productMapper.productToProductRes(existProductSaved);
            return BaseResponse.success(productResponse);
        }
        Product productSave = productMapper.productReqToEntity(productRequest);
        log.info("productSave: {} ", productSave);
        Category category = fetchCategory(productRequest.getCategory());
        productSave.setCategory(category);
        productSave.setStatus(true);
        Product productSaved = productRepository.save(productSave);
        ProductResponse productResponse = productMapper.productToProductRes(productSaved);
        Result response = new Result();
        response.setSuccessCode(0);
        response.setSuccessDescription("Success");
        BaseResponse<ProductResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(productResponse);
        baseResponse.setResult(response);
        return baseResponse;
    }

    public BaseResponse<PageResponse<ProductResponse>> getAllProducts(int page, int size, Integer category, Boolean status) {
        Pageable pageable = PageRequest.of(page, size);
        if (category == null && status == null) {
            status = true;
        }
        Page<Product> prodcts = productRepository.findProducts(category, status, pageable);
        log.info("prodcts: " + prodcts);
        log.info("prodcts: " + prodcts.getContent());
        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotalPages(prodcts.getTotalPages());
        pageResponse.setTotalElements(prodcts.getTotalElements());
        pageResponse.setData(
                prodcts.getContent()
                        .stream()
                        .map(productMapper::productToProductRes)
                        .collect(Collectors.toList())
        );
        Result response = new Result();
        response.setSuccessCode(0);
        response.setSuccessDescription("Success");
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(pageResponse);
        baseResponse.setResult(response);
        return baseResponse;
    }

    @Override
    public BaseResponse<ProductResponse> fetchByProductId(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ValidationException(1001, "Product not Found", "Product not Found"));
        Result response = new Result();
        response.setSuccessCode(0);
        response.setSuccessDescription("Success");
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(productMapper.productToProductRes(product));
        baseResponse.setResult(response);
        return baseResponse;
    }

    @Override
    public BaseResponse<ProductResponse> updateProduct(ProductRequest productRequest) {
        List<String> jwtRoles = jwtRequestContext.getRoles();
        commonService.roleAccessValidation(updateProductRoles, jwtRoles);
        productRequest.setName(productRequest.getName().toLowerCase());
        log.error("Product name:: {} ", productRequest.getName());
        Product product = commonService.findByProductNameAndStatus(productRequest.getName(), true);
        if (productRequest.getStock() != null) {
            product.setStock(product.getStock() + productRequest.getStock());
        }
        if (productRequest.getPrice() != null) {
            product.setPrice(productRequest.getPrice());
        }
        if (productRequest.getStatus() != null) {
            product.setStatus(productRequest.getStatus());
        }
        Product existProductSaved = productRepository.save(product);
        ProductResponse productResponse = productMapper.productToProductRes(existProductSaved);
        return BaseResponse.success(productResponse);
    }

    public Optional<Product> fetchByProductName(String name) {
        return productRepository.findByName(name);
    }

    public Category fetchCategory(Long category) {
        return categoryRepository.findById(category).orElseThrow(() -> new ValidationException(1002, "Category not found", "Category not found"));
    }
}

