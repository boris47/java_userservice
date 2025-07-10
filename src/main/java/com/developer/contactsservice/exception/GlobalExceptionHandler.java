package com.developer.contactsservice.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.developer.contactsservice.service.ContactsService.IllegalContactUpsert;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(IllegalContactUpsert.class)
	public ResponseEntity<?> handleIllegalContactUpsert(IllegalContactUpsert ex)
	{
		return switch (ex.illegal)
		{
			case NOT_FOUND -> ResponseEntity.notFound().build();
			case INCOMPLETE -> ResponseEntity.badRequest().body("ContactUpsert Incomplete");
			
			default -> ResponseEntity.internalServerError().build();
		};
	}
	
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ProblemDetail> handleMethodValidationException(HandlerMethodValidationException ex)
	{
		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problem.setTitle("Validation failed");
		problem.setDetail("One or more method argument validations failed");
		problem.setProperty("errors", ex.getAllErrors()
				.stream()
				.filter(error -> error instanceof FieldError)
				.map(FieldError.class::cast)
				.collect(Collectors.toMap(
						FieldError::getField,
						FieldError::getDefaultMessage,
						(msg1, msg2) -> msg1 // in caso di duplicati
				)));

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(problem);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> handleArgumentValidationException(MethodArgumentNotValidException ex)
	{
		Map<String, String> errors = new LinkedHashMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors())
		{
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		
		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problem.setTitle("Validation Failed");
		problem.setDetail("One or more fields failed validation");
		problem.setType(URI.create("https://example.com/problem/validation-error"));
		problem.setProperty("errors", errors);
		
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(problem);
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

