package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController
{    
	// @RequestHeader -> Legge header HTTP
	// @CookieValue -> Legge cookie HTTP
	// @PathVariable -> Legge variabili dal path URI, es: /users/{id}
	// @RequestParam -> Legge parametri da query string, es: /users?name=Roberto
	// @RequestBody -> Legge il corpo della richiesta (es. JSON) e lo deserializza in un oggetto Java
	// @ResponseBody -> Indica che il ritorno del metodo deve essere serializzato direttamente nella risposta HTTP
	// @ModelAttribute -> Usa i dati della richiesta per popolare un oggetto Java (form binding)
	
	private final UserService userService;
	
	
	// Test -> curl -X GET http://localhost:8081/api/users/getAllByIds?usersIds=ID1,ID2
	@GetMapping(path = "/getAllByIds", params = { "usersIds" })
	public ResponseEntity<List<User>> findAllById(@RequestParam List<String> usersIds)
	{
		if (!usersIds.isEmpty())
		{
			List<User> users = userService.findAllById(usersIds);
			if (!users.isEmpty())
			{
				return ResponseEntity.ok(users);
			}
		}
		return ResponseEntity.ok(Collections.emptyList());
	}
	
	// Test -> curl -X GET http://localhost:8081/api/users/getById?userId=ID1
	@GetMapping(path = "/getById", params = { "userId" })
	public ResponseEntity<User> getById(@RequestParam String userIdd)
	{
		return userService.findById(userIdd)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}
	
	// Test -> curl -X GET http://localhost:8081/api/users/getByName?name=NAME
	@GetMapping(path = "/getByName", params = { "name" })
	public List<User> getByName(@RequestParam String name)
	{
		return userService.findByName(name);
	}
	
	// Test -> curl -X GET http://localhost:8081/api/users/getByAge?age=XX
	@GetMapping(path = "/getByAge", params = { "age" })
	public List<User> getByAge(@RequestParam int age)
	{
		return userService.findByAge(age);
	}
	
	// Test -> curl -X POST http://localhost:8081/api/users/create -H "Content-Type: application/json" -d "{\"name\":\"jason\",\"age\":25,\"role\":\"GUEST\"}"
	@PostMapping(path = {"/create"})
	public ResponseEntity<String> create(@Valid @RequestBody UserDto userDto)
	{
		System.out.println("Upsert of user: " + userDto);
		
		userService.saveUser(userDto);
		
		return ResponseEntity.ok(String.format("User %s Created", userDto.getName()));
	}
	
	// Test -> 
	@DeleteMapping(path = {"/delete"}, params = { "userId" })
	public ResponseEntity<String> delete(@Valid @RequestParam String userId)
	{
		String outMsg;
		if (userService.deleteUser(userId))
		{
			outMsg = String.format("Deleted user %s", userId);
		}
		else
		{
			outMsg = String.format("User Not Found (%s)", userId);
		}
		
		return ResponseEntity.ok(String.format(outMsg));
	}
	
	@DeleteMapping(path = {"/massDelete"}, params = { "usersIds" })
	public ResponseEntity<String> massDelete(@Valid @RequestParam List<String> usersIds)
	{
		if (!usersIds.isEmpty())
		{
			userService.deleteAllById(usersIds);
		}
		
		return ResponseEntity.ok("Mass delete completed");
	}
	
	
	
	
	// Just usefull for collector and collections api
	public static <T> List<T> filter(Predicate<T> criteria, List<T> list)
	{
		return list.stream().filter(criteria).collect(Collectors.<T>toList());
	}
}
