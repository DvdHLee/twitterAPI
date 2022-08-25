package com.cooksys.twitter_api.services;

import java.util.List;

import com.cooksys.twitter_api.dtos.ContextDto;
import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.subentities.Credentials;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto newTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto getTweetById(Long id);

	void tweetLiked(Long id, Credentials credentials);

	TweetResponseDto deleteTweet(Long id, Credentials credentials);

	TweetResponseDto tweetReply(Long id, TweetRequestDto tweetRequestDto);
	
	List<TweetResponseDto> getReplies(Long id);


	TweetResponseDto repostTweet(Long id, Credentials credentials);

	List<HashtagDto> getTagsById(Long id);

	List<UserResponseDto> getLikesById(Long id);


	List<UserResponseDto> getMentions(Long id);


	ContextDto getContext(Long id);

	List<TweetResponseDto> getReposts(Long id);


}
