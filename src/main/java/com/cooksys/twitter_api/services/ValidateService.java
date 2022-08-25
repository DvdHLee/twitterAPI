package com.cooksys.twitter_api.services;

public interface ValidateService {

	boolean hashtagExists(String label);

	boolean usernameExists(String username);

	boolean usernameAvailable(String username);

}
