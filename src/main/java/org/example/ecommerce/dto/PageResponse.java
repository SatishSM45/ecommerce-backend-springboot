package org.example.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> data;
    int totalPages;
    long totalElements;
}
