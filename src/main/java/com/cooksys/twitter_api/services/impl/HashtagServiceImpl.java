package com.cooksys.twitter_api.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;

import com.cooksys.twitter_api.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagRepository hashtagRepository;
	
	private final HashtagMapper hashtagMapper;
	
	private final TweetMapper tweetMapper;
	
	//Return a list of all hashtags
	@Override
	public List<HashtagDto> getAllHashtags() {
		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
	}

	//Return a list of all tweets with a certain hashtag
	@Override
	public List<TweetResponseDto> getTweetsOfTag(String label) {
		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
		if (optionalHashtag.isEmpty()) {
			throw new NotFoundException("Tag not found");
		} else {
			return tweetMapper.entitiesToDtos(optionalHashtag.get().getTweets());
		}
	}

}
