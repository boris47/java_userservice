package com.developer.contactsservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.developer.contactsservice.model.Tag;

import jakarta.validation.constraints.NotNull;


public interface TagRepository extends JpaRepository<Tag, String>
{
    Optional<Tag> findByName(@NotNull String name);
}
