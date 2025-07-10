package com.developer.contactsservice;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.developer.contactsservice.repository.ContactsRepository;
import com.developer.contactsservice.service.ContactsService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class ContactServiceTest
{
	@Mock
	private ContactsRepository contactsRepository;
	
	@InjectMocks
	private ContactsService contactsService;
	
	@Test
	void shouldReturnContactWhenFound()
	{
		
	}
}
