package com.example.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.example.userservice.model.User;

public interface UserRepository extends JpaRepository<User, String>
{
	@NonNull Optional<User> findById(@NonNull String userId);
    List<User> findByName(String name);
    List<User> findByAge(int age);
   	@NonNull List<User> findAllById(@NonNull Iterable<String> usersIds);
	void deleteById(@NonNull String userId);
	void deleteAllById(@NonNull Iterable<? extends String> usersIds);
}