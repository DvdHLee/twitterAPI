package com.cooksys.twitter_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByCredentialsUsername(String username);

	List<User> findAllByDeletedFalse();

	Optional<User> findByCredentialsUsernameAndDeletedFalse(String username);

	Optional<User> findByCredentials(UserRequestDto userRequestDto);

	User findByCredentials(CredentialsDto credentials);

}
