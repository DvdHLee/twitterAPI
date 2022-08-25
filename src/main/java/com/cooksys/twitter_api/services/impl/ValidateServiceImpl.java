package com.cooksys.twitter_api.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	
	private final HashtagRepository hashtagRepository;
	
	private final UserRepository userRepository;
	
	//Checks if a Hashtag exits
	@Override
	public boolean hashtagExists(String label) {
		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
		if (optionalHashtag.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	//Checks is a username exists
	@Override
	public boolean usernameExists(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	//Checks if a username is available
	@Override
	public boolean usernameAvailable(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty() || optionalUser.get().isDeleted()) {
			return true;
		} else {
			return false;
		}
	}
	
}
