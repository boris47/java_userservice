package com.developer.contactsservice.model;

import java.util.ArrayList;
import java.util.List;

import com.developer.contactsservice.dto.ContactsDto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact
{
	@Id @Column(unique = true)
	private String id;
	
	// Mandatory
	private String name;
	private String surname;
	@Column(nullable = false)
	private String number;
	
	// Optional
	private String secondName;
	private String email;
	
	private String LastUpdate;
	private String ImageURL;
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(
		name = "contact_tags",
		joinColumns = @JoinColumn(name = "contact_id"),
		inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private List<Tag> tags = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)
	
	public static Contact from(String id, ContactsDto dto, List<Tag> tags)
	{
		Contact contact = new Contact();
		contact.setId(id);
		contact.setName(dto.getName());
		contact.setSurname(dto.getSurname());
		contact.setNumber(dto.getNumber());
		contact.setSecondName(dto.getSecondName());
		contact.setEmail(dto.getEmail());
		contact.setTags(tags);
		return contact;
	}
}
