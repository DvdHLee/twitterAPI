package com.cooksys.twitter_api.services;

import java.util.List;

import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.subentities.Credentials;

public interface UserService {
	
	List<UserResponseDto> getAllUsers();
	
	UserResponseDto getUser(String username);

	UserResponseDto createUser(UserRequestDto userRequestDto);

	UserResponseDto deleteUser(String username, Credentials credentials);

	UserResponseDto updateUser(String username, UserRequestDto userRequestDto);

	void follow(String username, Credentials credentials);

	void unfollow(String username, Credentials credentials);

	List<TweetResponseDto> feed(String username);

	List<TweetResponseDto> tweets(String username);

	List<TweetResponseDto> mentions(String username);

	List<UserResponseDto> followers(String username);

	List<UserResponseDto> following(String username);

//	ErrorDto error(int exception);

}
