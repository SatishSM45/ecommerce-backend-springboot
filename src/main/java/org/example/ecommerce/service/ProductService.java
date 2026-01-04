package org.example.ecommerce.service;

import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.dto.PageResponse;
import org.example.ecommerce.dto.ProductRequest;
import org.example.ecommerce.dto.ProductResponse;

public interface ProductService {
     BaseResponse<ProductResponse> addProduct(ProductRequest product);
    public BaseResponse<PageResponse<ProductResponse>> getAllProducts(int page, int size, Integer category, Boolean status);
    BaseResponse<ProductResponse> fetchByProductId(Long id);
    BaseResponse<ProductResponse> updateProduct(ProductRequest product);

}
