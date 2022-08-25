package com.cooksys.twitter_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.subentities.Credentials;
import com.cooksys.twitter_api.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<UserResponseDto> getAllUsers() {
	  return userService.getAllUsers();
	}
	
	@GetMapping("/@{username}")
	@ResponseStatus(HttpStatus.OK)
	public UserResponseDto getUser(@PathVariable String username) {
	  return userService.getUser(username);
	}
	  
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
		return userService.createUser(userRequestDto);
	}
	  
	@DeleteMapping("/@{username}")
	@ResponseStatus(HttpStatus.OK)
	public UserResponseDto deleteUser(@PathVariable String username, @RequestBody Credentials credentials) {
		return userService.deleteUser(username, credentials);
	}

	@PatchMapping("/@{username}")
	@ResponseStatus(HttpStatus.PARTIAL_CONTENT)
	public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
		return userService.updateUser(username, userRequestDto);
	}
	
	@PostMapping("/@{username}/follow")
	@ResponseStatus(HttpStatus.OK)
	public void follow(@PathVariable String username, @RequestBody Credentials credentials) {
		userService.follow(username, credentials);
	}
	
	@PostMapping("/@{username}/unfollow")
	@ResponseStatus(HttpStatus.OK)
	public void unfollow(@PathVariable String username, @RequestBody Credentials credentials) {
		userService.unfollow(username, credentials);
	}
	
	@GetMapping("/@{username}/feed")
	@ResponseStatus(HttpStatus.OK)
	public List<TweetResponseDto> feed(@PathVariable String username) {
		return userService.feed(username);
	}
	
	@GetMapping("/@{username}/tweets")
	@ResponseStatus(HttpStatus.OK)
	public List<TweetResponseDto> tweets(@PathVariable String username) {
		return userService.tweets(username);
	}
	
	@GetMapping("/@{username}/mentions")
	@ResponseStatus(HttpStatus.OK)
	public List<TweetResponseDto> mentions(@PathVariable String username) {
		return userService.mentions(username);
	}
	
	@GetMapping("/@{username}/followers")
	@ResponseStatus(HttpStatus.OK)
	public List<UserResponseDto> followers(@PathVariable String username) {
		return userService.followers(username);
	}
	
	@GetMapping("/@{username}/following")
	@ResponseStatus(HttpStatus.OK)
	public List<UserResponseDto> following(@PathVariable String username) {
		return userService.following(username);
	}
	
//	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
//	public ErrorDto error(int exception) {
//		return userService.error(exception);
//	}
}
