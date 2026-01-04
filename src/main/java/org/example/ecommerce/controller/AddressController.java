package org.example.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.dto.AddressRequest;
import org.example.ecommerce.dto.AddressResponse;
import org.example.ecommerce.dto.PageResponse;
import org.example.ecommerce.service.AddressService;
import org.example.ecommerce.service.AddressServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // Add address for user
    @PostMapping("/user")
    public ResponseEntity<AddressResponse> addAddress(
            @RequestBody AddressRequest request
    ) {
        return new ResponseEntity<>(addressService.addAddress(request), HttpStatus.CREATED);
    }

    // Get all addresses of user
    @GetMapping("/user")
    public ResponseEntity<BaseResponse<PageResponse<AddressResponse>>> fetchAllAddressesByUser(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {

        return ResponseEntity.ok(addressService.getAddressesByUser(page, size));
    }
    //TODO new api for the getAddressByAddressById.

    @GetMapping("/{addressId}")
    public ResponseEntity<BaseResponse<AddressResponse>> fetchAddressByAddressById(@PathVariable(name = "addressId") Long addressId) {
        return ResponseEntity.ok(addressService.getAddressByAddressById(addressId));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<BaseResponse> updateAddress(@PathVariable("addressId") Long addressId, @RequestBody AddressRequest addressRequest) {
        return ResponseEntity.ok(addressService.updateAddress(addressRequest, addressId));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<BaseResponse> deleteAddressById(@PathVariable long addressId) {
        return ResponseEntity.ok(addressService.deleteAddressById(addressId));
    }
}

