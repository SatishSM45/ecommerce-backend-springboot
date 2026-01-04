package org.example.ecommerce.service;

import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.dto.AddressRequest;
import org.example.ecommerce.dto.AddressResponse;
import org.example.ecommerce.dto.PageResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AddressService {
    AddressResponse addAddress(AddressRequest request);

    BaseResponse<PageResponse<AddressResponse>> getAddressesByUser(int page, int size);

    BaseResponse<AddressResponse> getAddressByAddressById(Long addressId);

    BaseResponse<Void> updateAddress(AddressRequest addressRequest, Long addressId);

    BaseResponse deleteAddressById(Long addressId);
}
