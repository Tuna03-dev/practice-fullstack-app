package org.example.exercise_shop.mapper;

import org.example.exercise_shop.dto.request.AddressCreationRequest;
import org.example.exercise_shop.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAdress(AddressCreationRequest addressCreationRequest);

}
