package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.ProductRequest;
import org.example.ecommerce.dto.ProductResponse;
import org.example.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category",ignore = true)
    Product productReqToEntity(ProductRequest productRequest);
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "categoryId", source = "category.id")
    ProductResponse  productToProductRes(Product product);
//    @Mapping(target ="categoryId",source = "product.id")
//    List<ProductResponse> productToProductResList(List<Product> product);
}
