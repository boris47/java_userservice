package com.developer.contactsservice.controller;

import com.developer.contactsservice.dto.ContactUpsertDto;
import com.developer.contactsservice.dto.ContactResponseDto;
import com.developer.contactsservice.model.Contact;
import com.developer.contactsservice.service.ContactsService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactsController
{    
	// @RequestHeader -> Legge header HTTP
	// @CookieValue -> Legge cookie HTTP
	// @PathVariable -> Legge variabili dal path URI, es: /contacts/{id}
	// @RequestParam -> Legge parametri da query string, es: /contacts?name=Roberto
	// @RequestBody -> Legge il corpo della richiesta (es. JSON) e lo deserializza in un oggetto Java
	// @ResponseBody -> Indica che il ritorno del metodo deve essere serializzato direttamente nella risposta HTTP
	// @ModelAttribute -> Usa i dati della richiesta per popolare un oggetto Java (form binding)
	
	private final ContactsService contactsService;
	
	
	// Test -> curl -X POST http://localhost:8081/api/contacts/upsert -H "Content-Type: application/json" -d "{\"name\":\"Mario\",\"surname\":\"Rossi\",\"number\":\"3334445678\"}"
	@PostMapping(path = { "/upsert" },
		consumes = {MediaType.APPLICATION_JSON_VALUE},
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<ContactResponseDto> upsert(@NotNull @Valid @RequestBody ContactUpsertDto dto)
	{
		System.out.println("Upsert of contact: " + dto);
		
		Contact contact = contactsService.upsert(dto);
		return contact != null ? ResponseEntity.ok(contactsService.toResponseDto(contact))
			: ResponseEntity.badRequest().build()
		;
	}
	
	// Test -> curl -X GET http://localhost:8081/api/contacts/delete?id=ID
	@DeleteMapping(path = {"/delete"}, params = { "id" },
		produces = {MediaType.TEXT_PLAIN_VALUE}
	)
	public ResponseEntity<String> delete(@NotBlank @RequestParam String id)
	{
		String outMsg;
		if (contactsService.deleteContact(id))
		{
			outMsg = String.format("Deleted contact %s", id);
		}
		else
		{
			outMsg = String.format("Contact Not Found (%s)", id);
		}
		
		return ResponseEntity.ok(outMsg);
	}
	
	// Test -> curl -X GET http://localhost:8081/api/contacts/massDelete -H "Content-Type: application/json" -d '["ID1", "ID2", ...]'
	@DeleteMapping(path = {"/massDelete"},
		consumes = MediaType.APPLICATION_JSON_VALUE,	
		produces = {MediaType.TEXT_PLAIN_VALUE}
	)
	public ResponseEntity<String> massDelete(@RequestBody List<@NotBlank String> ids)
	{
		if (!ids.isEmpty())
		{
			contactsService.deleteAllById(ids);
		}
		
		return ResponseEntity.ok("Mass delete completed");
	}
	
	// Test -> curl -X GET http://localhost:8081/api/contacts/findAllById -H "Content-Type: application/json" -d '["ID1", "ID2", ...]'
	@GetMapping(path = "/findAllById",
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<Page<ContactResponseDto>> findAllById(
		@RequestBody List<@NotBlank String> ids,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	)
	{
		if (ids != null && !ids.isEmpty())
		{
			return ResponseEntity.ok(contactsService.findAllById(ids, page, size).map(c -> contactsService.toResponseDto(c)));
		}
		return ResponseEntity.ok(Page.empty());
	}
	
	// Test -> curl -X GET http://localhost:8081/api/contacts/getById?id=ID1
	@GetMapping(path = "/findById", params = { "id" },
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<Contact> findById(@NotBlank @RequestParam String id)
	{
		return contactsService.findById(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}
	
	// Test -> curl -X GET http://localhost:8081/api/contacts/getByNumber?number=XYZ...
	@GetMapping(path = "/getByNumber", params = { "number" },
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<Page<Contact>> getByNumber(
		@NotBlank @RequestParam String number,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size)
	{
		return ResponseEntity.ok(contactsService.findByNumber(number, page, size));
	}
	
	// Test -> curl -X GET http://localhost:8081/api/contacts/byTag?tag=family&page=0&size=15
	@GetMapping(path = "/findByTag", params = { "tag", "page", "size" },
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<Page<Contact>> findByTag(
			@NotBlank @RequestParam String tag,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size)
	{
		return ResponseEntity.ok(contactsService.findByTag(tag, page, size));
	}
	
	// Test -> curl -X GET http://localhost:8081/api/contacts/byTags?tags=work&tags=family&page=0&size=15
	@GetMapping(path = "/findByTags", params = { "tags", "page", "size" },
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<Page<Contact>> findByTags(
			@NotNull @NotEmpty @RequestParam List<@NotBlank String> tags,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size)
	{
		return ResponseEntity.ok(contactsService.findByTags(tags, page, size));
	}
	
	// Test -> curl -X GET http://localhost:8081/api/contacts/getByName?name=NAME
	@GetMapping(path = "/getByName", params = { "name" },
		produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<Page<Contact>> getByName(
		@NotBlank @RequestParam String name,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size)
	{
		return ResponseEntity.ok(contactsService.findByName(name, page, size));
	}
}
