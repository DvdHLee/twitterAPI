package com.cooksys.twitter_api.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.entities.subentities.Credentials;
import com.cooksys.twitter_api.entities.subentities.Profile;
import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.exceptions.NotAuthorizedException;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.UserService;
import com.cooksys.twitter_api.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final TweetMapper tweetMapper;
	private final ValidateService validateService;

	private User findUser(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("No user found with the username: " + username);
		}

		return optionalUser.get();
	}

	private void checkCredentialsDto(String username, UserRequestDto userRequestDto) {
		User verifyUser = findUser(username);
		User verifyCredentials = userMapper.userRequestDtoToEntity(userRequestDto);
		if (!verifyCredentials.getCredentials().equals(verifyUser.getCredentials())) {
			throw new NotAuthorizedException("Invalid credentials: " + userRequestDto);
		}
	}
	
	private void checkCredentials(Credentials credentials) {
		User user = findUser(credentials.getUsername());
		
		if (!user.getCredentials().equals(credentials)) {
			throw new NotAuthorizedException("Invalid credentials: " + credentials);
		}
	}

	@Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
	}

	@Override
	public UserResponseDto getUser(String username) {
		User userToFind = findUser(username);
		return userMapper.entityToDto(userToFind);
	}

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		User userToSave = userMapper.userRequestDtoToEntity(userRequestDto);
		if (userToSave.getProfile() == null || userToSave.getCredentials() == null
				|| userToSave.getCredentials().getUsername() == null
				|| userToSave.getCredentials().getPassword() == null || userToSave.getProfile().getEmail() == null) {
			throw new BadRequestException(
					"Username, password, or email were left blank. Please fill all required fields.");
		}
		boolean userExist = validateService.usernameExists(userToSave.getCredentials().getUsername());
		boolean userAvailable = validateService.usernameAvailable(userToSave.getCredentials().getUsername());
		if (!userAvailable) {
			throw new BadRequestException("User already exists");
		} else if (userExist) {
			User recreateUser = userRepository.findByCredentialsUsername(userToSave.getCredentials().getUsername()).get();
			recreateUser.setDeleted(false);
			return userMapper.entityToDto(userRepository.saveAndFlush(recreateUser));
		}

		return userMapper.entityToDto(userRepository.saveAndFlush(userToSave));
	}

	@Override
	public UserResponseDto deleteUser(String username, Credentials credentials) {
		User userToDelete = findUser(username);
		
		checkCredentials(credentials);

		userToDelete.setDeleted(true);
		return userMapper.entityToDto(userRepository.saveAndFlush(userToDelete));
	}

	@Override
	public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
		User userToUpdate = findUser(username);
		User updates = userMapper.userRequestDtoToEntity(userRequestDto);

		if (updates.getProfile() == null || updates.getCredentials() == null) {
			throw new BadRequestException("Username, password, or email were left blank. Please fill all required fields.");
		}
		
		checkCredentialsDto(username, userRequestDto);
		
		Profile profile = userToUpdate.getProfile();
		
		if (updates.getProfile().getEmail() != null) {
			profile.setEmail(updates.getProfile().getEmail());
		}
		if (updates.getProfile().getFirstName() != null) {
			profile.setFirstName(updates.getProfile().getFirstName());
		}
		if (updates.getProfile().getLastName() != null) {
			profile.setLastName(updates.getProfile().getLastName());
		}
		if (updates.getProfile().getPhone() != null) {
			profile.setPhone(updates.getProfile().getPhone());
		}
		
		userToUpdate.setProfile(profile);
		
		return userMapper.entityToDto(userRepository.saveAndFlush(userToUpdate));
	}

	@Override
	public void follow(String username, Credentials credentials) {
		checkCredentials(credentials);
		User userToFollow = findUser(username);
		User follower = findUser(credentials.getUsername());
		
		List<User> following = follower.getFollowing();
		if (following.contains(userToFollow)) {
			throw new BadRequestException("Already following the user!");
		} else {
			following.add(userToFollow);
		}
		
		follower.setFollowing(following);
		
		userRepository.saveAndFlush(follower);
	}

	@Override
	public void unfollow(String username, Credentials credentials) {
		checkCredentials(credentials);
		User userToUnfollow = findUser(username);
		User unfollower = findUser(credentials.getUsername());
		
		List<User> following = unfollower.getFollowing();
		if (following.contains(userToUnfollow)) {
			following.remove(userToUnfollow);
		} else {
			throw new BadRequestException("Not following the user!");
		}
		
		unfollower.setFollowing(following);
		
		userRepository.saveAndFlush(unfollower);
	}

	@Override
	public List<TweetResponseDto> feed(String username) {
		User user = findUser(username);
		List<Tweet> feed = user.getTweets();
		for (User follower : user.getFollowers()) {
			feed.addAll(follower.getTweets());
		}
		Collections.sort(feed);
		Collections.reverse(feed);
		return tweetMapper.entitiesToDtos(feed);
	}

	@Override
	public List<TweetResponseDto> tweets(String username) {
		User user = findUser(username);
		List<Tweet> tweets = user.getTweets();
		Collections.sort(tweets);
		Collections.reverse(tweets);
		return tweetMapper.entitiesToDtos(tweets);
	}

	@Override
	public List<TweetResponseDto> mentions(String username) {
		User user = findUser(username);
		List<Tweet> mentions = user.getMentions();
		Collections.sort(mentions);
		Collections.reverse(mentions);
		return tweetMapper.entitiesToDtos(mentions);
	}

	@Override
	public List<UserResponseDto> followers(String username) {
		User user = findUser(username);
		return userMapper.entitiesToDtos(user.getFollowers());
	}

	@Override
	public List<UserResponseDto> following(String username) {
		User user = findUser(username);
		return userMapper.entitiesToDtos(user.getFollowing());
	}

}
