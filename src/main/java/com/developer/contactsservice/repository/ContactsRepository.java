package com.developer.contactsservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.developer.contactsservice.model.Contact;

public interface ContactsRepository extends JpaRepository<Contact, String>
{
	@NonNull Optional<Contact> findById(@NonNull String id);
	Page<Contact> findByNumber(@NonNull String number, Pageable pageable);
	
	@Query("SELECT c FROM Contact c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	Page<Contact> findByName(@Param("name") String name, Pageable pageable);
	
	@Query("SELECT c FROM Contact c WHERE c.id IN :ids")
	Page<Contact> findAllById(@Param("ids") List<String> ids, Pageable pageable);
	
	void deleteById(@NonNull String id);
	void deleteAllById(@NonNull Iterable<? extends String> ids);
	
	@Query("SELECT DISTINCT c FROM Contact c JOIN c.tags t WHERE t.name = :name")
	Page<Contact> findByTags_Name(@Param("name") String name, Pageable pageable);
	
	@Query("SELECT DISTINCT c FROM Contact c JOIN c.tags t WHERE t.name IN :names")
	Page<Contact> findByTags_Names(@Param("names") List<String> tagNames, Pageable pageable);
}