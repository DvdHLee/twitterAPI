package com.cooksys.twitter_api.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {

	@Id
	@GeneratedValue
	private Long id;
	
	private String label;
	
	private Timestamp firstUsed = Timestamp.valueOf(LocalDateTime.now());
	
	private Timestamp lastUsed = Timestamp.valueOf(LocalDateTime.now());
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Tweet> tweets;
	
}
