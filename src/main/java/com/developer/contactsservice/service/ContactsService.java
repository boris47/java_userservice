package com.developer.contactsservice.service;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.developer.contactsservice.Util.UniqueIdGenerator;
import com.developer.contactsservice.dto.ContactUpsertDto;
import com.developer.contactsservice.dto.ContactResponseDto;
import com.developer.contactsservice.model.Contact;
import com.developer.contactsservice.model.Tag;
import com.developer.contactsservice.repository.ContactsRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactsService
{
	private static final Sort kSort = Sort.by(Direction.ASC, "name");
	
	private final ContactsRepository contactRepository;
	private final TagService tagService;
	private final ModelMapper modelMapper;
	
	
	public Contact upsert(ContactUpsertDto dto)
    {
		Contact contact;
		Optional<Contact> existing;
		
		if (dto.uniqueId() != null) // Upsert
		{
			existing = contactRepository.findById(dto.uniqueId());
			if (existing.isEmpty())
			{
				return null;
			}
			
			contact = existing.get();
			
			if (dto.name() != null) contact.setName(dto.name());
			if (dto.surname() != null) contact.setSurname(dto.surname());
			if (dto.number() != null) contact.setNumber(dto.number());
			contact.setSecondName(dto.secondName());
			contact.setEmail(dto.email());
			contact.setImageURL(dto.ImageURL());
		}
		else // Create
		{
			if (dto.name() == null || dto.number() == null)
			{
				return null;
			}
			
			String uniqueId = UniqueIdGenerator.GenerateUniqueID();
			contact = modelMapper.map(dto, Contact.class);
			contact.setId(uniqueId);
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
	
	public ContactResponseDto toResponseDto(Contact contact)
    {
		if (contact == null) return null;
		
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
	
	
	

	public boolean deleteContact(String contactId)
	{
		boolean bOutValue;
		if (bOutValue = contactRepository.existsById(contactId))
		{
			contactRepository.deleteById(contactId);
		}
		return bOutValue;
	}

	public void deleteAllById(List<String> ids)
	{
		contactRepository.deleteAllById(ids);
	}

	public Page<Contact> findAllById(List<String> contactsIds, int page, int size)
	{
		Pageable pageable = PageRequest.of(page, size, kSort);
		return contactRepository.findAllById(contactsIds, pageable);
	}

	public Page<Contact> findByName(String name, int page, int size)
	{
		Pageable pageable = PageRequest.of(page, size, kSort);
		return contactRepository.findByName(name, pageable);
	}

	public Optional<Contact> findById(String id)
	{
		return contactRepository.findById(id);
	}
	public Page<Contact> findByNumber(String number, int page, int size)
	{
		Pageable pageable = PageRequest.of(page, size, kSort);
		return contactRepository.findByNumber(number, pageable);
	}
	
	public Page<Contact> findByTag(String tagName, int page, int size)
	{
		Pageable pageable = PageRequest.of(page, size, kSort);
		return contactRepository.findByTags_Name(tagName, pageable);
	}
	
	public Page<Contact> findByTags(List<String> tagNames, int page, int size)
	{
		Pageable pageable = PageRequest.of(page, size, kSort);
		return contactRepository.findByTags_Names(tagNames, pageable);
	}
}
