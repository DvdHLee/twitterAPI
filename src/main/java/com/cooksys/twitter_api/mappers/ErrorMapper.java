package com.cooksys.twitter_api.mappers;

import org.mapstruct.Mapper;

import com.cooksys.twitter_api.dtos.ErrorDto;

@Mapper(componentModel = "spring")
public interface ErrorMapper {
	
	ErrorDto entityToDto(Error entity);
	
}
