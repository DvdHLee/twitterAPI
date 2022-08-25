package com.cooksys.twitter_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter_api.dtos.ContextDto;
import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.subentities.Credentials;
import com.cooksys.twitter_api.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {

	private final TweetService tweetService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<TweetResponseDto> getAllTweets() {
		return tweetService.getAllTweets();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public TweetResponseDto getTweetById(@PathVariable Long id) {
		return tweetService.getTweetById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto newTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.newTweet(tweetRequestDto);
	}

	@PostMapping("/{id}/like")
	@ResponseStatus(HttpStatus.CREATED)
	public void tweetLiked(@PathVariable Long id, @RequestBody Credentials credentials) {
		tweetService.tweetLiked(id, credentials);
	}
	
	@PostMapping("/{id}/reply")
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto tweetReply(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.tweetReply(id, tweetRequestDto);
	}
	
	@PostMapping("/{id}/repost")
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto repostTweet(@PathVariable Long id, @RequestBody Credentials credentials) {
		return tweetService.repostTweet(id, credentials);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public TweetResponseDto deleteTweet(@PathVariable Long id, @RequestBody Credentials credentials ) {
		return tweetService.deleteTweet(id, credentials);
	}
	
	@GetMapping("/{id}/replies")
	@ResponseStatus(HttpStatus.OK)
	public List<TweetResponseDto> getReplies(@PathVariable Long id) {
		return tweetService.getReplies(id);
	}
	
	@GetMapping("/{id}/reposts")
	@ResponseStatus(HttpStatus.OK)
	public List<TweetResponseDto> getReposts(@PathVariable Long id) {
		return tweetService.getReposts(id);
	}
	
	@GetMapping("/{id}/tags")
	@ResponseStatus(HttpStatus.OK)
	public List<HashtagDto> getTagsById(@PathVariable Long id) {
		return tweetService.getTagsById(id);
	}
	
	@GetMapping("/{id}/likes")
	@ResponseStatus(HttpStatus.OK)
	public List<UserResponseDto> getLikesById(@PathVariable Long id) {
		return tweetService.getLikesById(id);
	}
	

	@GetMapping("/{id}/mentions")
	@ResponseStatus(HttpStatus.OK)
	public List<UserResponseDto> getMentions(@PathVariable Long id){
		return tweetService.getMentions(id);
	}

	@GetMapping("/{id}/context")
	@ResponseStatus(HttpStatus.OK)
	public ContextDto getContext(@PathVariable Long id) {
		return tweetService.getContext(id);
	}


}
