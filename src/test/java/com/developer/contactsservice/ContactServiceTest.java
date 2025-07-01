package com.developer.contactsservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.developer.contactsservice.model.Contact;
import com.developer.contactsservice.repository.ContactsRepository;
import com.developer.contactsservice.service.ContactsService;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest
{
	@Mock
	private ContactsRepository contactsRepository;
	
	@InjectMocks
	private ContactsService contactsService;
	
	@Test
	void shouldReturnContactWhenFound()
	{
		final String testID = "Aldo";
		
		Contact contact = new Contact();
		contact.setId(testID);

		Mockito.when(contactsRepository.findById(testID)).thenReturn(Optional.of(contact));

		Optional<Contact> result = contactsService.findById(testID);
		assertNotNull(result);
		assertEquals(testID, result.get().getId());
	}
}
