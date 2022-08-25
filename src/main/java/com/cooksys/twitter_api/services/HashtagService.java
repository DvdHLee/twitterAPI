package com.cooksys.twitter_api.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;

public interface HashtagService {

	List<HashtagDto> getAllHashtags();

	List<TweetResponseDto> getTweetsOfTag(String label);
	
}
