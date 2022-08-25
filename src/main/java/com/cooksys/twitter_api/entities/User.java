package com.cooksys.twitter_api.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.cooksys.twitter_api.entities.subentities.Credentials;
import com.cooksys.twitter_api.entities.subentities.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_table")
public class User {
	
	@Id
	@GeneratedValue
	private Long id;
		
	@Embedded
	private Profile profile;
	
	@Embedded
	private Credentials credentials;
	
	@CreationTimestamp
	private Timestamp joined = Timestamp.valueOf(LocalDateTime.now());
	
	private boolean deleted;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private List<Tweet> tweets;
	
	@ManyToMany(mappedBy = "following", cascade = CascadeType.ALL)
	private List<User> followers;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> following;
	
	@ManyToMany(mappedBy = "likes", cascade = CascadeType.ALL)
	private List<Tweet> likedTweets;
	
	@ManyToMany(mappedBy = "mentionedUsers", cascade = CascadeType.ALL)
	private List<Tweet> mentions;
	
}
