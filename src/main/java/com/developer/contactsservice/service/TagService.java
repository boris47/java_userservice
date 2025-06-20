package com.developer.contactsservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.developer.contactsservice.model.Tag;
import com.developer.contactsservice.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService
{
	private final TagRepository tagRepository;

	public Tag findOrCreate(String name)
	{
		return tagRepository.findByName(name)
				.orElseGet(() -> tagRepository.save(new Tag(name)));
	}

	public List<Tag> resolveTags(List<String> names)
	{
		return names.stream()
				.filter(name -> name != null && !name.isBlank())
				.map(this::findOrCreate)
				.toList();
	}
}