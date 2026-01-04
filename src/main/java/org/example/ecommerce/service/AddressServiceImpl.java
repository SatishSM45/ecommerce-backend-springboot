package org.example.ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.common.CommonConstants;
import org.example.ecommerce.common.Result;
import org.example.ecommerce.config.JwtRequestContext;
import org.example.ecommerce.dto.AddressRequest;
import org.example.ecommerce.dto.AddressResponse;
import org.example.ecommerce.dto.PageResponse;
import org.example.ecommerce.entity.Address;
import org.example.ecommerce.entity.User;
import org.example.ecommerce.mapper.AddressMapper;
import org.example.ecommerce.repository.AddressRepository;
import org.example.ecommerce.repository.UserRepository;
import org.example.ecommerce.util.CommonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final CommonService commonService;
    private final JwtRequestContext jwtRequestContext;
    private final AddressMapper addressMapper;

    @Transactional
    public AddressResponse addAddress(AddressRequest request) {
        String email = jwtRequestContext.getEmail();
        User user = commonService.findByEmail(email);
        // If default address → remove existing default
        if (request.getDefaultAddress() != null && request.getDefaultAddress()) {
            Optional<Address> defaultAddressOptional = addressRepository.findByUserIdAndIsDefaultTrue(user.getId());
            if (defaultAddressOptional.isPresent()) {
                Address defaultAddress = defaultAddressOptional.get();
                defaultAddress.setDefault(false);
                addressRepository.save(defaultAddress);
            }
        }
        Address address = new Address();
        address.setFullName(request.getFullName());
        address.setMobileNo(request.getMobileNo());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPinCode(request.getPinCode());
        address.setAddressType(request.getAddressType());
        address.setDefault(request.getDefaultAddress());
        address.setUser(user);
        log.info("saving the address: {} ", address);
        Address saved = addressRepository.save(address);
        return addressMapper.entityToResponse(saved);
    }

    public BaseResponse<PageResponse<AddressResponse>> getAddressesByUser(int page, int size) {
        //take the user email from the jwtToken
        String email = jwtRequestContext.getEmail();
        User user = commonService.findByEmail(email);
        Pageable pageable = PageRequest.of(page, size);
        Page<Address> addressResponse = addressRepository.findByUser_Id(user.getId(), pageable);
        PageResponse<AddressResponse> addressResponsePageResponse = new PageResponse<>();
        addressResponsePageResponse.setData(addressResponse.getContent().stream().map(addressMapper::entityToResponse).collect(Collectors.toList()));
        addressResponsePageResponse.setTotalElements(addressResponse.getTotalElements());
        addressResponsePageResponse.setTotalPages(addressResponse.getTotalPages());
        Result response = new Result();
        response.setSuccessCode(CommonConstants.SUCCESS_CODE);
        response.setSuccessDescription(CommonConstants.SUCCESS);
        BaseResponse<PageResponse<AddressResponse>> baseResponse = new BaseResponse<>();
        baseResponse.setData(addressResponsePageResponse);
        baseResponse.setResult(response);
        return baseResponse;
    }

    @Override
    public BaseResponse<AddressResponse> getAddressByAddressById(Long addressId) {
        log.info("enter into the address service addressId: {} ", addressId);
        String email = jwtRequestContext.getEmail();
        User user = commonService.findByEmail(email);
        Address address = commonService.findAddressByAddressId(addressId);
        AddressResponse addressResponse = addressMapper.entityToResponse(address);
        Result response = new Result();
        response.setSuccessCode(CommonConstants.SUCCESS_CODE);
        response.setSuccessDescription(CommonConstants.SUCCESS);
        BaseResponse<AddressResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(addressResponse);
        baseResponse.setResult(response);
        log.info("addressResponse : {}", addressResponse);
        return baseResponse;
    }

    @Override
    public BaseResponse updateAddress(AddressRequest addressRequest, Long addressId) {
        String email = jwtRequestContext.getEmail();
        User user = commonService.findByEmail(email);
        if (addressRequest.getDefaultAddress()) {
            Optional<Address> addressOptional = addressRepository.findByUser_IdAndIsDefault(user.getId(), true);
            if (addressRequest.getDefaultAddress() != null && addressRequest.getDefaultAddress()) {
                Address defaultAddress = addressOptional.get();
                defaultAddress.setDefault(false);
                addressRepository.save(defaultAddress);
            }
        }
        Address address = commonService.findAddressByAddressId(addressId);
        if (StringUtils.hasText(addressRequest.getFullName())) {
            address.setFullName(addressRequest.getFullName());
        }
        if (StringUtils.hasText(addressRequest.getMobileNo())) {
            address.setMobileNo(addressRequest.getMobileNo());
        }
        if (StringUtils.hasText(addressRequest.getAddressLine1())) {
            address.setAddressLine1(addressRequest.getAddressLine1());
        }
        if (StringUtils.hasText(addressRequest.getAddressLine2())) {
            address.setAddressLine2(addressRequest.getAddressLine2());
        }
        if (StringUtils.hasText(addressRequest.getCity())) {
            address.setCity(addressRequest.getCity());
        }
        if (StringUtils.hasText(addressRequest.getState())) {
            address.setState(addressRequest.getState());
        }
        if (StringUtils.hasText(addressRequest.getCountry())) {
            address.setCountry(addressRequest.getCountry());
        }
        if (StringUtils.hasText(addressRequest.getPinCode())) {
            address.setPinCode(addressRequest.getPinCode());
        }
        if (!ObjectUtils.isEmpty(addressRequest.getAddressType())) {
            address.setAddressType(addressRequest.getAddressType());
        }
        addressRepository.save(address);
        return BaseResponse.success(null);
    }

    @Override
    public BaseResponse deleteAddressById(Long addressId) {
        log.info("deleteBy addressId: {} ", addressId);
        //User validation
        Address address = commonService.findAddressByAddressId(addressId);
        addressRepository.delete(address);
        return BaseResponse.success(null);
    }


}

