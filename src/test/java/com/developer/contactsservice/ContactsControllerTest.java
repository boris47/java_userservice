package com.developer.contactsservice;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class ContactsControllerTest
{
	@Autowired
	private MockMvc mockMvc;
	
	@Test @Order(1)
	void Test01()
	{
		var req = post("/api/contacts/contact")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
							"uniqueId": "114734329050498697",
							"number": "3378"
						}
						""");
		try {
			mockMvc.perform(req)
					.andExpect(status().isBadRequest())
					.andDo(print())
					.andExpect(jsonPath("$.errors").exists());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
