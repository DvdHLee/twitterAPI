package com.cooksys.twitter_api.mappers;

import org.mapstruct.Mapper;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.entities.subentities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

	CredentialsDto entityToDto(Credentials subentity);
	
}
