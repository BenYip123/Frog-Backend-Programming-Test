package com.frog.frogbackendprogrammingtest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.frog.frogbackendprogrammingtest.datamodel.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	// Find all books by title
	List<Book> findByTitle(String title);
	
	// Find a book by ISBN
	Optional<Book> findByIsbn(String isbn);
	
	// Check if an ISBN already exists
	boolean existsByIsbn(String isbn);
}
