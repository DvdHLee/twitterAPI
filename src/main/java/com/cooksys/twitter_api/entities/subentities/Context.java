package com.cooksys.twitter_api.entities.subentities;

import java.util.List;

import com.cooksys.twitter_api.entities.Tweet;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Context {

	private Tweet target;
	
	private List<Tweet> before;
	
	private List<Tweet> after;
	
}
