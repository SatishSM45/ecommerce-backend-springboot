package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.AddressResponse;
import org.example.ecommerce.entity.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressResponse entityToResponse(Address address);
}
