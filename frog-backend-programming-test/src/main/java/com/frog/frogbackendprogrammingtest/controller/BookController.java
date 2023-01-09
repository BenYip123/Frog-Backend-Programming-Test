package com.frog.frogbackendprogrammingtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.frog.frogbackendprogrammingtest.datamodel.Book;
import com.frog.frogbackendprogrammingtest.repository.BookRepository;

// Controller class to return a domain object
@RestController
@RequestMapping("/api")
public class BookController {

	@Autowired
	BookRepository bookRepository;
	
	// Create logger
	Logger logger = Logger.getLogger(BookController.class.getName());

	// Gets all books by title. If no title provided then return all books
	@GetMapping("/books")
	public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) String title) {
		logger.info("getAllBooks with " + title);
		try {
			List<Book> books = new ArrayList<Book>();
			if (title == null) {
				logger.info("find all books");
				bookRepository.findAll().forEach(books::add);
			} else {
				logger.info("find all books with title");
				bookRepository.findByTitle(title).forEach(books::add);
			}
			if (books.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(books, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Get a book by its ID
	@GetMapping(value = "/books", params = { "id" })
	public ResponseEntity<Book> getBookById(@RequestParam final long id) {
		logger.info("getBookById with " + id);
		try {
			Optional<Book> book = bookRepository.findById(id);
			if (book.isPresent()) {
				return new ResponseEntity<>(book.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Get a book by its ISBN
	@GetMapping(value = "/books", params = { "isbn" })
	public ResponseEntity<Book> getBookByIsbn(@RequestParam final String isbn) {
		logger.info("getBookByIsbn with " + isbn);
		try {
			Optional<Book> book = bookRepository.findByIsbn(isbn);
			if (book.isPresent()) {
				return new ResponseEntity<>(book.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// Create a new book
	@PostMapping("/books")
	public ResponseEntity<Book> createBook(@RequestBody Book book){
		logger.info("createBook with " + book.toString());
		try {
			Book _book = bookRepository.save(new Book(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getPublished_year(), book.getPage_count(), book.getRetail_price(), book.getDescription()));
			return new ResponseEntity<>(book, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// Update a specific book by ID
	@PutMapping("/books/{id}")
	public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book book) {
		logger.info("updateBook with " + book.toString());
		Optional<Book> bookData = bookRepository.findById(id);

		if (bookData.isPresent()) {
			Book _book = bookData.get();
			_book.setIsbn(book.getIsbn());
			_book.setTitle(book.getTitle());
			_book.setAuthor(book.getAuthor());
			_book.setPublished_year(book.getPublished_year());
			_book.setPage_count(book.getPage_count());
			_book.setRetail_price(book.getRetail_price());
			_book.setDescription(book.getDescription());
			return new ResponseEntity<>(bookRepository.save(_book), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	// Delete book by a specific ID
	@DeleteMapping("/books/{id}")
	public ResponseEntity<HttpStatus> deleteBooks(@PathVariable("id") long id) {
		logger.info("deleteBooks with " + id);
		try {
			bookRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// Delete all books
	@DeleteMapping("/books")
	public ResponseEntity<HttpStatus> deleteAllBooks() {
		logger.info("deleteAllBooks");
		try {
			bookRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
