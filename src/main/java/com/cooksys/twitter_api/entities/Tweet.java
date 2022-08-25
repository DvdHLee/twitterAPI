package com.cooksys.twitter_api.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Tweet implements Comparable<Tweet> {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "author_id")
	private User author;
	
	private Timestamp posted = Timestamp.valueOf(LocalDateTime.now());
	
	private String content;
	
	@ManyToOne
//	@JoinColumn(name = "inReplyTo_id")
	private Tweet inReplyTo;
	
	@OneToMany(mappedBy = "inReplyTo", cascade = CascadeType.ALL)
	private List<Tweet> replies;
	
	@ManyToOne
//	@JoinColumn(name = "repostOf_id")
	private Tweet repostOf;
	
	@OneToMany(mappedBy = "repostOf", cascade = CascadeType.ALL)
	private List<Tweet> reposts;
	
	private boolean deleted;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> likes;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> mentionedUsers;
	
	@ManyToMany(mappedBy = "tweets", cascade = CascadeType.MERGE)
	private List<Hashtag> hashtags;

	@Override
	public int compareTo(Tweet t) {
		if (getPosted() == null || t.getPosted() == null) {
			return 0;
		}
		return getPosted().compareTo(t.getPosted());
	}
	
}
