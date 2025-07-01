package com.developer.contactsservice.model;

import java.util.ArrayList;
import java.util.List;

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
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String surname;
	@Column(nullable = false)
	private String number;
	
	// Optional
	private String secondName;
	private String email;
	
	// Internal Handled
	private String lastUpdate;
	private String imageURL;
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(
		name = "contact_tags",
		joinColumns = @JoinColumn(name = "contact_id"),
		inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private List<Tag> tags = new ArrayList<>();
}
