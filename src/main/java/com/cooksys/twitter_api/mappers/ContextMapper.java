package com.cooksys.twitter_api.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.twitter_api.dtos.ContextDto;
import com.cooksys.twitter_api.entities.subentities.Context;

@Mapper(componentModel = "spring")
public interface ContextMapper {
	
	ContextDto entityToDto(Context subentity);
	
	List<ContextDto> entitiesToDtos(List<Context> subentites);
	
}
