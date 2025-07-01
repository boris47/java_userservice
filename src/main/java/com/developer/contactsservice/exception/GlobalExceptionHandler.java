package com.developer.contactsservice.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.developer.contactsservice.service.ContactsService.IllegalContactUpsert;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(IllegalContactUpsert.class)
	public ResponseEntity<Void> handleIllegalContactUpsert(IllegalContactUpsert ex)
	{
		return switch (ex.illegal)
		{
			case NOT_FOUND -> ResponseEntity.notFound().build();
			case INCOMPLETE -> ResponseEntity.badRequest().build();
			
			default -> ResponseEntity.internalServerError().build();
		};
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationException(MethodArgumentNotValidException ex)
	{
		Map<String, String> errors = new LinkedHashMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors())
		{
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problemDetail.setTitle("Validation Failed");
		problemDetail.setDetail("One or more fields failed validation");
		problemDetail.setType(URI.create("https://example.com/problem/validation-error"));
		problemDetail.setProperty("errors", errors);
		
		return problemDetail;
		
	//	Map<String, String> errors = new HashMap<>();
	//	for (FieldError fieldError : ex.getBindingResult().getFieldErrors())
	//	{
	//		errors.put(fieldError.getField(), fieldError.getDefaultMessage());
	//	}
	//	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex)
	{
		Map<String, String> errors = new HashMap<>();

		for (ConstraintViolation<?> violation : ex.getConstraintViolations())
		{
			String path = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			errors.put(path, message);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleUnreadable(HttpMessageNotReadableException ex)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request body");
	}
}

