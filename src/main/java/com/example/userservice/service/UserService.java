package com.example.userservice.service;

import com.example.userservice.Util.UniqueIdGenerator;
import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService
{
	private final UserRepository userRepository;
	
	public List<User> getAllUsers()
	{
		return null;//kUsers;
	}
	
	public void saveUser(UserDto userDto)
	{
		String id = UniqueIdGenerator.GenerateUniqueID();
		User user = User.From(id, userDto);
		userRepository.save(user);
	}
	
	public boolean deleteUser(String userId)
	{
		boolean bOutValue;
		if (bOutValue = userRepository.existsById(userId))
		{
			userRepository.deleteById(userId);
		}
		return bOutValue;
	}
	
	public void deleteAllById(List<String> ids)
	{
		userRepository.deleteAllById(ids);
	}
	
	public List<User> findAllById(Iterable<String> usersIds)
	{
		return userRepository.findAllById(usersIds);
	}
	
	public List<User> findByName(String name)
	{
		return userRepository.findByName(name);
	}

	public Optional<User> findById(String id)
	{
		return userRepository.findById(id);
	}

	public List<User> findByAge(int age)
	{
		return userRepository.findByAge(age);
	}
}
