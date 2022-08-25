package com.cooksys.twitter_api.entities.subentities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
public class Profile {
	
	private String firstName;
	
	private String lastName;
	
	@Column(nullable = false)
	private String email;
	
	private String phone;
	
}
