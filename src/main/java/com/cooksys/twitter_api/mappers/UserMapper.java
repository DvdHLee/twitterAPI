package com.cooksys.twitter_api.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.User;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {
	
	User userRequestDtoToEntity(UserRequestDto userRequestDto);

//	@Mapping(source = "credentials.username", target = "username")
//	UserResponseDto entityToDto(Optional<User> optional);
	
	@Mapping(source = "credentials.username", target = "username")
	UserResponseDto entityToDto(User userToFind);
	
	List<UserResponseDto> entitiesToDtos(List<User> entities);
	
}
