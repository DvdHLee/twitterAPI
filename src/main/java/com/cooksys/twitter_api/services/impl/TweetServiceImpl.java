package com.cooksys.twitter_api.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.cooksys.twitter_api.dtos.ContextDto;
import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.entities.subentities.Credentials;
import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.exceptions.NotAuthorizedException;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class TweetServiceImpl implements TweetService {

	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final HashtagRepository hashtagRepository;
	private final HashtagMapper hashtagMapper;

	private void validateTweetRequest(TweetRequestDto tweetRequestDto) {
		//Tweet tweetToCheck = tweetMapper.tweetRequestDtoToEntity(tweetRequestDto);
		if (tweetRequestDto.getCredentials() == null || tweetRequestDto.getCredentials().getUsername() == null
				|| tweetRequestDto.getCredentials().getPassword() == null || tweetRequestDto.getContent() == null) {
			throw new BadRequestException("Something you entered is null, try again.");
		}

	}

	private Tweet findTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("Tweet with Id of " + id + " was not found");
		}

		return optionalTweet.get();
	}

	private User findUser(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("No user was found with the username: " + username);
		}

		return optionalUser.get();
	}
	
	private Hashtag findHashtag(String label) {
		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
		if (optionalHashtag.isEmpty()) {
			throw new NotFoundException("No tag was found with the hashtag: " + label);
		}
		
		return optionalHashtag.get();
	}

	private void checkCredentials(Credentials credentials) {
		User user = findUser(credentials.getUsername());

		if (!user.getCredentials().equals(credentials)) {
			throw new NotAuthorizedException("Invalid credentials: " + credentials);
		}
	}

	@Override
	public List<TweetResponseDto> getAllTweets() {
		return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalse());
	}

	@Override
	public TweetResponseDto newTweet(TweetRequestDto tweetRequestDto) {
		if (tweetRequestDto.getCredentials() == null || tweetRequestDto.getCredentials().getUsername() == null
				|| tweetRequestDto.getCredentials().getPassword() == null || tweetRequestDto.getContent() == null) {
			throw new BadRequestException("Something you entered is null, try again.");
		}

		Tweet tweetToCreate = tweetMapper.tweetRequestDtoToEntity(tweetRequestDto);
		User findAuthor = findUser(tweetRequestDto.getCredentials().getUsername());
		tweetToCreate.setAuthor(findAuthor);
		tweetToCreate.setContent(tweetRequestDto.getContent());
		
		//Find All mentioned users in content
		String regexString =  "@((?=.[A-Za-z\\d])[\\w_]+)";
		Pattern pattern = Pattern.compile(regexString);
		Matcher matcher = pattern.matcher(tweetToCreate.getContent());
		List<User> mentionedUsers = new ArrayList<>();
		while(matcher.find()) {
			String username = matcher.group(1);
			User userToAdd = findUser(username);
			mentionedUsers.add(userToAdd);
		}
		tweetToCreate.setMentionedUsers(mentionedUsers);
		
		//Find All mentioned tags in content
		String regexStringTag =  "#((?=.[A-Za-z\\d])[\\w_]+)";
		Pattern patternTag = Pattern.compile(regexStringTag);
		Matcher matcherTag = patternTag.matcher(tweetToCreate.getContent());
		List<Hashtag> hashtags = new ArrayList<>();
		while(matcherTag.find()) {
			String hashtagLabel = matcherTag.group(1);
			try {
				Hashtag hashtag = findHashtag(hashtagLabel);
				List<Tweet> hashtagTweets = hashtag.getTweets();
				hashtagTweets.add(tweetToCreate);
				hashtag.setTweets(hashtagTweets);
				hashtags.add(hashtag);
				hashtagRepository.saveAndFlush(hashtag);
			} catch (NotFoundException e) {
				Hashtag hashtagToAdd = new Hashtag();
				hashtagToAdd.setLabel(hashtagLabel);
				List<Tweet> hashtagToAddTweets = new ArrayList<>();
				hashtagToAddTweets.add(tweetToCreate);
				hashtagToAdd.setTweets(hashtagToAddTweets);
				hashtags.add(hashtagToAdd);
				hashtagRepository.saveAndFlush(hashtagToAdd);
			}

		}
		tweetToCreate.setHashtags(hashtags);

		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToCreate));
	}

	@Override
	public TweetResponseDto getTweetById(Long id) {
		Tweet tweet = findTweet(id);
		return tweetMapper.entityToDto(tweet);
	}

	@Override
	public void tweetLiked(Long id, Credentials credentials) {
		Tweet tweetToLike = findTweet(id);
		checkCredentials(credentials);

		List<User> tweetLikes = tweetToLike.getLikes();
		User userToAdd = findUser(credentials.getUsername());
		
		if(!tweetLikes.contains(userToAdd)) {
			tweetLikes.add(userToAdd);
		}
		
		tweetRepository.saveAndFlush(tweetToLike);
	}

	@Override
	public TweetResponseDto deleteTweet(Long id, Credentials credentials) {
		Tweet tweetToDelete = findTweet(id);
		checkCredentials(credentials);

		tweetToDelete.setDeleted(true);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToDelete));
	}

	@Override
	public TweetResponseDto tweetReply(Long id, TweetRequestDto tweetRequestDto) {
		Tweet tweetToReplyTo = findTweet(id);

		validateTweetRequest(tweetRequestDto);
		
		User user = findUser(tweetRequestDto.getCredentials().getUsername());
		Tweet replyTweet = tweetMapper.tweetRequestDtoToEntity(tweetRequestDto);
		List<Tweet> tweetReplies = tweetToReplyTo.getReplies();
		
		replyTweet.setAuthor(user);
		replyTweet.setInReplyTo(tweetToReplyTo);
		tweetReplies.add(replyTweet);

		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(replyTweet));

	}

	@Override
	public TweetResponseDto repostTweet(Long id, Credentials credentials) {
		Tweet tweetToRepost = findTweet(id);
		TweetRequestDto tweetRequest = tweetMapper.entityToRequestDto(tweetToRepost);
		checkCredentials(credentials);
		Tweet repostedTweet = tweetMapper.tweetRequestDtoToEntity(tweetRequest);

		User user = findUser(credentials.getUsername());
		repostedTweet.setAuthor(user);
		repostedTweet.setContent(tweetToRepost.getContent());
		repostedTweet.setRepostOf(tweetToRepost);

		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(repostedTweet));
	}
	
	@Override
	public List<TweetResponseDto> getReplies(Long id) {
		Tweet tweetToFind = findTweet(id);
		List<Tweet> repliesToTweet = tweetToFind.getReplies();
		
		return tweetMapper.entitiesToDtos(repliesToTweet);
	}
	
	@Override
	public List<TweetResponseDto> getReposts(Long id) {
		Tweet tweetToFind = findTweet(id); 
		List<Tweet> repostsToTweet = tweetToFind.getReposts();
		
		return tweetMapper.entitiesToDtos(repostsToTweet);
	}

	@Override
	public List<HashtagDto> getTagsById(Long id) {
		Tweet tweetToFind = findTweet(id);
		List<Hashtag> hashtagList = tweetToFind.getHashtags();
			
		return hashtagMapper.entitiesToDtos(hashtagList);
	}

	@Override
	public List<UserResponseDto> getLikesById(Long id) {
		Tweet tweetToFind = findTweet(id);
		List<User> likedBy = tweetToFind.getLikes();

		return userMapper.entitiesToDtos(likedBy);
	}

	@Override
	public List<UserResponseDto> getMentions(Long id) {
		Tweet tweetToFind = findTweet(id);
		List<User> mentionedUsers = tweetToFind.getMentionedUsers();
			
		return userMapper.entitiesToDtos(mentionedUsers);
	}

	@Override
	public ContextDto getContext(Long id) {
		Tweet tweetToFind = findTweet(id);
		ContextDto context = new ContextDto();
		List<TweetResponseDto> listBefore = new ArrayList<>();
		Tweet currentTweet = tweetToFind;
		
		context.setTarget(tweetMapper.entityToDto(tweetToFind));
		context.setAfter(tweetMapper.entitiesToDtos(tweetToFind.getReplies()));
		
		while(currentTweet.getInReplyTo() != null) {
			listBefore.add(tweetMapper.entityToDto(currentTweet.getInReplyTo()));
			currentTweet = currentTweet.getInReplyTo();
		}
		
		context.setBefore(listBefore);
		
		return context;
	}

}
