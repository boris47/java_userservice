package com.developer.contactsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.developer.contactsservice.Util.UniqueIdGenerator;
import com.developer.contactsservice.dto.ContactUpsertDto;
import com.developer.contactsservice.dto.ContactResponseDto;
import com.developer.contactsservice.model.Contact;
import com.developer.contactsservice.model.Tag;
import com.developer.contactsservice.repository.ContactsRepository;
import com.developer.contactsservice.service.ContactsService.IllegalContactUpsert.Illegal;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactsService
{
	private final ContactsRepository contactRepository;
	private final TagService tagService;
	private final ModelMapper modelMapper;
	
	
	public Page<Contact> findByNameOrSurnameOrNumber(String query, Pageable pageable)
	{
		if (query != null && !query.isBlank())
		{
			return contactRepository.findByNameOrSurnameOrNumber(query, pageable);
		}
		
		return contactRepository.findAll(pageable);
	//	if (query != null && query.length() > 0)
	//	{
	//		Contact probe = new Contact();
	//		{
	//			probe.setName(query);
	//			probe.setSurname(query);
	//			probe.setNumber(query);
	//		}
	//		
	//		ExampleMatcher matcher = ExampleMatcher.matchingAny()
	//				.withMatcher("name", match -> match.contains().ignoreCase())
	//				.withMatcher("surname", match -> match.contains().ignoreCase())
	//				.withMatcher("number", match -> match.contains().ignoreCase())
	//		;
	//		
	//		Example<Contact> example = Example.of(probe, matcher);
	//		Pageable pageable = PageRequest.of(page, size, kSort);
	//		return contactRepository.findAll(example, pageable);
	//	}
	//	
	//	// Not filtered
	//	Pageable pageable = PageRequest.of(page, size, kSort);
	//	return contactRepository.findAll(pageable);
	}
	
	
	////////////////////////////////////////////////////////////////////////
	
	
	public Contact upsert(ContactUpsertDto dto) throws IllegalContactUpsert
    {
		Contact contact;
		Optional<Contact> existing;
		
		if (dto.uniqueId() != null) // Upsert
		{
			log.debug("Trying to upsert contact " + dto.uniqueId());
			existing = contactRepository.findById(dto.uniqueId());
			if (existing.isEmpty())
			{
				throw new IllegalContactUpsert(Illegal.NOT_FOUND);
			}
			
			contact = existing.get();
			
			// Debug purpose
			String name = contact.getName();
			
			if (dto.name() != null) contact.setName(dto.name());
			if (dto.surname() != null) contact.setSurname(dto.surname());
			if (dto.number() != null) contact.setNumber(dto.number());
			contact.setSecondName(dto.secondName());
			contact.setEmail(dto.email());
			contact.setImageURL(dto.ImageURL());
			
			log.debug("Update of " + name + " completed");
		}
		else // Create
		{
			if (dto.name() == null || dto.surname() == null || dto.number() == null)
			{
				log.error("Incomplete creation body");
				throw new IllegalContactUpsert(Illegal.INCOMPLETE);
			}
			
			log.debug("Creation of new contact named " + dto.name());
			
			String uniqueId = UniqueIdGenerator.GenerateUniqueID();
			contact = modelMapper.map(dto, Contact.class);
			contact.setId(uniqueId);
			
			log.debug("Contact created" + contact);
		}
		
		if (dto.tags() != null)
		{
			// Empty Array set contact tags to empty array as well
			contact.getTags().clear();
			
			// If dto has tags, then add them
			if (!dto.tags().isEmpty())
			{
				contact.getTags().addAll(tagService.resolveTags(dto.tags()));
			}
		}
		
		{
			String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
				.format(new java.util.Date())
			;
			contact.setLastUpdate(timeStamp);
		}
		
        return contactRepository.save(contact);
    }
	
	public boolean deleteContact(String contactId)
	{
		boolean bOutValue;
		if (bOutValue = contactRepository.existsById(contactId))
		{
			contactRepository.deleteById(contactId);
		}
		return bOutValue;
	}

	public void deleteAllById(String[] ids)
	{
		contactRepository.deleteAllById(ids);
	}

	public Page<Contact> findAllById(String[] contactsIds, Pageable pageable)
	{
		return contactRepository.findAllById(contactsIds, pageable);
	}

	public Page<Contact> findByName(String name, Pageable pageable)
	{
		return contactRepository.findByName(name, pageable);
	}

	public Optional<Contact> findById(String id)
	{
		return contactRepository.findById(id);
	}
	public Page<Contact> findByNumber(String number, Pageable pageable)
	{
		if (number.length() > 0)
		{
			return contactRepository.findByNumber(number, pageable);
		}
		return Page.empty();
	}
	
	public Page<Contact> findByTag(String tagName, Pageable pageable)
	{
		return contactRepository.findByTags_Name(tagName, pageable);
	}
	
	public Page<Contact> findByTags(String[] tagNames, Pageable pageable)
	{
		return contactRepository.findByTags_Names(tagNames, pageable);
	}
	
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	
	public class IllegalContactUpsert extends RuntimeException
	{
		public enum Illegal
		{
			NOT_FOUND,
			INCOMPLETE
		}
		
		public final Illegal illegal;
		
		public IllegalContactUpsert(Illegal InIllegal)
		{
			super();
			
			illegal = InIllegal;
		}
	}
	
	public static ContactResponseDto toResponseDto(Contact contact)
	{
		return new ContactResponseDto(
			contact.getId(),
			contact.getName(),
			contact.getSurname(),
			contact.getNumber(),
			contact.getLastUpdate(),
			contact.getImageURL(),
			contact.getTags().stream().map(Tag::getName).toArray(String[]::new)
		);
	}
}
