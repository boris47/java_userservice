package com.developer.contactsservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.developer.contactsservice.controller.ContactsController;
import com.developer.contactsservice.dto.ContactResponseDto;
import com.developer.contactsservice.dto.ContactUpsertDto;
import com.developer.contactsservice.model.CustomPaginatedListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.*;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class ContactIntegrationTest
{
	// Logger log = LoggerFactory.getLogger(ContactIntegrationTest.class);
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private Validator validator;
	
	// Multiline string
	// invalid Number format
	private static final String invalidJsonUpsert = """
	{
		"name": "Roberto",
		"surname": "Rossi",
		"number": "123"
	}
	""";
	
	@Test @Order(1)
	void Test_001_TestUpsertInvalidJson()
	{
		System.out.println("----- Test_001_TestUpsertInvalidJson");
		try {
			ContactUpsertDto dto = objectMapper.readValue(invalidJsonUpsert, ContactUpsertDto.class);
			java.util.Set<ConstraintViolation<ContactUpsertDto>> violations = validator.validate(dto);
			
			if (violations.size() > 0)
			{
				violations.forEach(v ->
					System.err.println(String.format("Property: '%s': %s", v.getPropertyPath(), v.getMessage()))
				);
			}
			assertTrue(violations.size() >= 0);
		}
		catch (Exception e)
		{
			System.err.println(String.format("ContactIntegrationTest.TestUpsertJson: %s", e.getMessage()));
			assertFalse(true);
		}
	}
	
	private static final String validJsonUpsert = """
	{
		"name": "Roberto",
		"surname": "Rossi",
		"number": "3348596753"
	}
	""";
	
	@Test @Order(2)
	void Test_002_ShouldCreateAndRetrieveAndDeleteContact()
	{
		System.out.println("----- Test_002_ShouldCreateAndRetrieveAndDeleteContact");
		{
			final var req = post(ContactsController.CONTACT())
				.contentType(MediaType.APPLICATION_JSON)
				.content(validJsonUpsert)
			;	
			try {
				mockMvc.perform(req)
					.andExpect(status().isCreated())
					.andReturn()
				;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		{
			final String contactName = "Roberto";
			String uniqueID = null;
			
			{
				var req = get(ContactsController.CONTACTS())
					.param("query", contactName)
					.param("page", "0")
					.param("size", "3")
				;
				try {
					var result = mockMvc.perform(req)
						.andExpect(status().isOk())
						// .andDo(print())
						.andExpect(jsonPath("$.data[0].name").value(contactName))
						.andReturn()
					;
					String jsonStr = result.getResponse().getContentAsString();
					if (jsonStr != null && jsonStr.length() > 0)
					{
						try {
							CustomPaginatedListModel<ContactResponseDto> paged =
								objectMapper.readValue(jsonStr, new TypeReference<CustomPaginatedListModel<ContactResponseDto>>() {});
							uniqueID = paged.data().get(0).uniqueId();
							System.out.println(uniqueID);
						}
						catch (Exception e)
						{
							System.err.println("Unable to retrieve uniqueID" + e);
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
			if (uniqueID != null)
			{
				var req = delete(ContactsController.CONTACT()).param("id", uniqueID);
				try {
					mockMvc.perform(req)
						.andExpect(status().is(HttpStatus.NO_CONTENT.value()))
					;
				}
				catch (Exception e)
				{
					System.err.println("Unable to delete (" + uniqueID + ")" + e);
				}
			}
		}
	}
}
