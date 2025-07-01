package com.developer.contactsservice.controller;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.developer.contactsservice.dto.ContactResponseDto;
import com.developer.contactsservice.dto.ContactUpsertDto;
import com.developer.contactsservice.model.Contact;
import com.developer.contactsservice.model.CustomPaginatedListModel;
import com.developer.contactsservice.service.ContactsService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ContactsController.BASE/*"/api/contacts"*/)
@RequiredArgsConstructor
public class ContactsController
{
	public static final String BASE = "/api/contacts";
	private static final int kPageDefaultSize = 10;
	
	// @RequestHeader -> Legge header HTTP
	// @CookieValue -> Legge cookie HTTP
	// @PathVariable -> Legge variabili dal path URI, es: /contacts/{id}
	// @RequestParam -> Legge parametri da query string, es: /contacts?name=Roberto
	// @RequestBody -> Legge il corpo della richiesta (es. JSON) e lo deserializza in un oggetto Java
	// @ResponseBody -> Indica che il ritorno del metodo deve essere serializzato direttamente nella risposta HTTP
	// @ModelAttribute -> Usa i dati della richiesta per popolare un oggetto Java (form binding)
	
	// @NotNull -> (CharSequence |Collection | Map |Array) Not null but Empty allowed
	// @NotEmpty -> (CharSequence |Collection | Map |Array) Not Null and Not Empty (its size/length must be greater than zero)
	// @NotBlank -> (String) Not Null and trimmed length must be greater than zero
	
	private final ContactsService contactsService;
	
	
	private static final String _CONTACTS = "/contacts";
	private static final String _FIND_ALL_BY_IDS = "/findAllByIds";
	
	public static final String CONTACTS() { return BASE + _CONTACTS; };
	public static final String FIND_ALL_BY_IDS() { return BASE + _FIND_ALL_BY_IDS; };
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Test -> curl -X GET http://localhost:8081/api/contacts/contacts
	// Test -> curl -X GET http://localhost:8081/api/contacts/contacts?query=giacomo
	// Test -> curl -X GET http://localhost:8081/api/contacts/contacts?query=giacomo&page=0&size=15
	// Test -> curl -X GET http://localhost:8081/api/contacts/contacts?query=giacomo&page=0&size=15&sort=name,asc
	@GetMapping(path = _CONTACTS,
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	@ResponseBody
	public ResponseEntity<CustomPaginatedListModel<ContactResponseDto>> contacts
	(
		@RequestParam(defaultValue = "")
			String query,
		@PageableDefault(page = 0, size = kPageDefaultSize, sort = "name", direction = Sort.Direction.ASC)
    		Pageable pageable
	)
	{
		Page<Contact> contactsPage = contactsService.findByNameOrSurnameOrNumber(query, pageable);
		return ResponseEntity.ok(CustomPaginatedListModel.fromPage(contactsPage.map(ContactsService::toResponseDto)));
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Test -> curl -X GET http://localhost:8081/api/contacts/massDelete?ids=ID1,ID2
	@DeleteMapping(path = _CONTACTS, params = { "ids" },
		produces = {MediaType.TEXT_PLAIN_VALUE}
	)
	public ResponseEntity<String> massDelete
	(
		@RequestBody(required = true) @NotBlank
			String ids
	)
	{
		contactsService.deleteAllById(ids.split(","));
		return ResponseEntity.noContent().build();
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Test -> curl -X GET http://localhost:8081/api/contacts/findAllByIds?ids=ID1,ID2
	@GetMapping(path = _FIND_ALL_BY_IDS, params = { "ids" },
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<CustomPaginatedListModel<ContactResponseDto>> findAllByIds
	(
		@RequestParam(required = true) @NotBlank
			String ids,
		@PageableDefault(page = 0, size = kPageDefaultSize, sort = "name", direction = Sort.Direction.ASC)
    		Pageable pageable
	)
	{
		Page<Contact> contactsPage = contactsService.findAllById(ids.split((",")), pageable);
		return ResponseEntity.ok(CustomPaginatedListModel.fromPage(contactsPage.map(ContactsService::toResponseDto)));
	}
	
	
	private static final String _CONTACT = "/contact";
	private static final String _FIND_BY_ID = "/findById";
	private static final String _FIND_BY_NAME = "/findByName";
	private static final String _FIND_BY_NUMBER = "/findByNumber";
	private static final String _FIND_BY_TAG = "/findByTag";
	private static final String _FIND_BY_TAGS = "/findByTags";
	
	public static String CONTACT() { return BASE + _CONTACT; }
	public static String FIND_BY_ID() { return BASE + _FIND_BY_ID; }
	public static String FIND_BY_NAME() { return BASE + _FIND_BY_NAME; }
	public static String FIND_BY_NUMBER() { return BASE + _FIND_BY_NUMBER; }
	public static String FIND_BY_TAG() { return BASE + _FIND_BY_TAG; }
	public static String FIND_BY_TAGS() { return BASE + _FIND_BY_TAGS; }
	
	////////////////////////////////////////////////////////////////////////
	// Test -> curl -X POST http://localhost:8081/api/contacts/contact -H "Content-Type: application/json" -d "{\"name\":\"Mario\",\"surname\":\"Rossi\",\"number\":\"3334445678\"}"
	// Test -> curl -X POST http://localhost:8081/api/contacts/contact -H "Content-Type: application/json" -d "{\"uniqueId\":\"XXXXXXXXXXXXX\",\"number\":\"3334445678\"}"
	@PostMapping(path = _CONTACT,
		consumes = {MediaType.APPLICATION_JSON_VALUE},
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<ContactResponseDto> upsert
	(
		@RequestBody(required = true) @NotNull @Valid
			ContactUpsertDto dto
	)
	{
		Contact contact = contactsService.upsert(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ContactsService.toResponseDto(contact));
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Test -> curl -X GET http://localhost:8081/api/contacts/contact?id=ID
	@DeleteMapping(path = _CONTACT, params = { "id" },
		produces = {MediaType.TEXT_PLAIN_VALUE}
	)
	public ResponseEntity<String> delete
	(
		@RequestParam(required = true) @NotBlank
			String id
	)
	{
		if (contactsService.deleteContact(id))
		{
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.notFound().build();
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Test -> curl -X GET http://localhost:8081/api/contacts/findById?id=ID1
	@GetMapping(path = _FIND_BY_ID, params = { "id" },
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<Contact> findById
	(
		@RequestParam(required = true) @NotBlank
			String id
	)
	{
		return contactsService.findById(id).map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Test -> curl -X GET http://localhost:8081/api/contacts/findByName?name=NAME
	@GetMapping(path = _FIND_BY_NAME, params = { "name" },
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<CustomPaginatedListModel<ContactResponseDto>> findByName
	(
		@NotBlank @RequestParam
			String name,
		@PageableDefault(page = 0, size = kPageDefaultSize, sort = "name", direction = Sort.Direction.ASC)
    		Pageable pageable
	)
	{
		Page<Contact> contactsPage = contactsService.findByName(name, pageable);
		return ResponseEntity.ok(CustomPaginatedListModel.fromPage(contactsPage.map(ContactsService::toResponseDto)));
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Test -> curl -X GET http://localhost:8081/api/contacts/findByNumber?number=XYZ...
	@GetMapping(path = _FIND_BY_NUMBER, params = { "number" },
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<CustomPaginatedListModel<ContactResponseDto>> findByNumber
	(
		@RequestParam @NotNull
			String number,
		@PageableDefault(page = 0, size = kPageDefaultSize, sort = "name", direction = Sort.Direction.ASC)
    		Pageable pageable
	)
	{
		Page<Contact> contactsPage = contactsService.findByNumber(number, pageable);
		return ResponseEntity.ok(CustomPaginatedListModel.fromPage(contactsPage.map(ContactsService::toResponseDto)));
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Test -> curl -X GET http://localhost:8081/api/contacts/byTag?tag=family&page=0&size=15
	@GetMapping(path = _FIND_BY_TAG, params = { "tag" },
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<CustomPaginatedListModel<ContactResponseDto>> findByTag
	(
		@NotBlank @RequestParam
			String tag,
		@PageableDefault(page = 0, size = kPageDefaultSize, sort = "name", direction = Sort.Direction.ASC)
    		Pageable pageable
	)
	{
		Page<Contact> contactsPage = contactsService.findByTag(tag, pageable);
		return ResponseEntity.ok(CustomPaginatedListModel.fromPage(contactsPage.map(ContactsService::toResponseDto)));
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Test -> curl -X GET http://localhost:8081/api/contacts/findByTags?tags=work,family&page=0&size=15
	@GetMapping(path = _FIND_BY_TAGS, params = { "tags" },
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<CustomPaginatedListModel<ContactResponseDto>> findByTags
	(
		@NotNull @NotEmpty @RequestParam @NotBlank
			String tags,
		@PageableDefault(page = 0, size = kPageDefaultSize, sort = "name", direction = Sort.Direction.ASC)
    		Pageable pageable
	)
	{
		Page<Contact> contactsPage = contactsService.findByTags(tags.split(","), pageable);
		return ResponseEntity.ok(CustomPaginatedListModel.fromPage(contactsPage.map(ContactsService::toResponseDto)));
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
	}
}
