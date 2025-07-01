package com.developer.contactsservice.repository;

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
	
	// 'LIKE' pattern matching means "contains" case-sensitive, ex: WHERE name LIKE '%ann%'
	// (PostgreSQL Only) 'ILIKE' pattern matching means "contains" no-case-sensitive, ex: WHERE name ILIKE '%ann%'
	// <> is for different, ex: WHERE name <> 'Anna'
	// '':name' is the query paramater, passed by the API (Here String name)
	// '%'' is any sequence of characters
	@Query("SELECT c FROM Contact c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))") // Contains
	//@Query("SELECT c FROM Contact c WHERE LOWER(c.name) LIKE LOWER(CONCAT(:name, '%'))") // Starts with
	//@Query("SELECT c FROM Contact c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name))") // Ends with
	Page<Contact> findByName(@Param("name") String name, Pageable pageable);
	
	@Query("SELECT c FROM Contact c " +
       "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :value, '%')) " +
       "OR LOWER(c.surname) LIKE LOWER(CONCAT('%', :value, '%')) " +
       "OR LOWER(c.number) LIKE LOWER(CONCAT('%', :value, '%'))")
	Page<Contact> findByNameOrSurnameOrNumber(@Param("value") String value, Pageable pageable);
	
	@Query("SELECT c FROM Contact c WHERE c.id IN :ids")
	Page<Contact> findAllById(@Param("ids") String[] ids, Pageable pageable);
	
	void deleteById(@NonNull String id);
	void deleteAllById(@NonNull String[] ids);
	
	@Query("SELECT DISTINCT c FROM Contact c JOIN c.tags t WHERE t.name = :name")
	Page<Contact> findByTags_Name(@Param("name") String name, Pageable pageable);
	
	@Query("SELECT DISTINCT c FROM Contact c JOIN c.tags t WHERE t.name IN :names")
	// Use @Param only for names who differ from paramter name like here
	Page<Contact> findByTags_Names(@Param("names") String[] tagNames, Pageable pageable);
}