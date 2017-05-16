package com.example.domain.repository;

import com.example.domain.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by grisha on 25.02.17.
 */
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
