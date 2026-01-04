package org.example.ecommerce.controller;

import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.dto.PageResponse;
import org.example.ecommerce.dto.ProductRequest;
import org.example.ecommerce.dto.ProductResponse;
import org.example.ecommerce.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping
    public ResponseEntity<BaseResponse<ProductResponse>> addProduct(@RequestBody ProductRequest product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<ProductResponse>>> fetchProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "category", required = false) Integer category,
            @RequestParam(name = "status", defaultValue = "true") Boolean status
    ) {
        return ResponseEntity.ok(productService.getAllProducts(page, size, category, status));
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> fetchProductByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.fetchByProductId(id));
    }

    // TODO Pending
    @PutMapping
    public ResponseEntity<BaseResponse<ProductResponse>> updateProduct(@RequestBody ProductRequest product) {
        return ResponseEntity.ok(productService.updateProduct(product));
    }

}

