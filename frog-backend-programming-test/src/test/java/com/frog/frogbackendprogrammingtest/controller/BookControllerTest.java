package com.frog.frogbackendprogrammingtest.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frog.frogbackendprogrammingtest.datamodel.Book;
import com.frog.frogbackendprogrammingtest.repository.BookRepository;

@WebMvcTest(BookController.class)
public class BookControllerTest {
	@MockBean
	private BookRepository bookRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	// Create logger
	Logger logger = Logger.getLogger(BookControllerTest.class.getName());

	// Tests to check if a book is created
	@Test
	void createBookTest() throws Exception {
		logger.info("Tests to check if a book is created");
		Book book = new Book("9781847624826", "Pride and Prejudice", "Jane Austen", 2010, 285, new BigDecimal(4.50),
				"Pride and Prejudice is a novel by Jane Austen that satirizes issues of marriage and social class.");

		mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(book))).andExpect(status().isCreated()).andDo(print());
	}

	// Checks if all books are returned
	@Test
	void returnListOfBooksTest() throws Exception {
		logger.info("Checks if all books are returned");
		List<Book> books = new ArrayList<>(Arrays.asList(new Book("9781847624826", "Pride and Prejudice", "Jane Austen",
				2010, 285, new BigDecimal(4.50),
				"Pride and Prejudice is a novel by Jane Austen that satirizes issues of marriage and social class."),
				new Book("9780099419785", "To Kill A Mockingbird", "Harper Lee", 1989, 320, new BigDecimal(8.99),
						"To Kill a Mockingbird views the brutality of racism in the Deep South through the eyes of the big-hearted child narrator, Scout."),
				new Book("9780141187761", "Nineteen Eighty-Four", "George Orwell", 2004, 384, new BigDecimal(8.99),
						"Who controls the past controls the future: who controls the present controls the past.")));

		when(bookRepository.findAll()).thenReturn(books);
		mockMvc.perform(get("/api/books")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(books.size())).andDo(print());
	}

	// Returns a book from a specific ID and checks all attributes are correct
	@Test
	void returnBookById() throws Exception {
		logger.info("Returns a book from a specific ID and checks all attributes are correct");
		long id = 1L;
		Book book = new Book(1L, "9781847624826", "Pride and Prejudice", "Jane Austen", 2010, 285, new BigDecimal(4.50),
				"Pride and Prejudice is a novel by Jane Austen that satirizes issues of marriage and social class.");

		when(bookRepository.findById(id)).thenReturn(Optional.of(book));
		mockMvc.perform(get("/api/books?id={id}", id)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.isbn").value(book.getIsbn()))
				.andExpect(jsonPath("$.title").value(book.getTitle()))
				.andExpect(jsonPath("$.author").value(book.getAuthor()))
				.andExpect(jsonPath("$.published_year").value(book.getPublished_year()))
				.andExpect(jsonPath("$.page_count").value(book.getPage_count()))
				.andExpect(jsonPath("$.retail_price").value(book.getRetail_price()))
				.andExpect(jsonPath("$.description").value(book.getDescription())).andDo(print());
	}

	// Tests a specific ID does not exist in the database and correctly identifies
	// the status as not found
	@Test
	void returnNotFoundBook() throws Exception {
		logger.info(
				"Tests a specific ID does not exist in the database and correctly identifies the status as not found");
		long id = 1L;

		when(bookRepository.findById(id)).thenReturn(Optional.empty());
		mockMvc.perform(get("/api/books?id={id}", id)).andExpect(status().isNotFound()).andDo(print());
	}

	// Test to return a book by ISBN and check all attributes are correct
	@Test
	void returnBookByIsbn() throws Exception {
		logger.info("Test to return a book by ISBN and check all attributes are correct");
		String isbn = "9781847624826";
		Book book = new Book("9781847624826", "Pride and Prejudice", "Jane Austen", 2010, 285, new BigDecimal(4.50),
				"Pride and Prejudice is a novel by Jane Austen that satirizes issues of marriage and social class.");

		when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
		mockMvc.perform(get("/api/books?isbn={isbn}", isbn)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(book.getId())).andExpect(jsonPath("$.isbn").value(book.getIsbn()))
				.andExpect(jsonPath("$.title").value(book.getTitle()))
				.andExpect(jsonPath("$.author").value(book.getAuthor()))
				.andExpect(jsonPath("$.published_year").value(book.getPublished_year()))
				.andExpect(jsonPath("$.page_count").value(book.getPage_count()))
				.andExpect(jsonPath("$.retail_price").value(book.getRetail_price()))
				.andExpect(jsonPath("$.description").value(book.getDescription())).andDo(print());
	}

	// Tests update on a book that already exists and check all attributes are
	// correct
	@Test
	void updateBook() throws Exception {
		logger.info("Tests update on a book that already exists and check all attributes are correct");
		long id = 1L;

		Book book = new Book("9781847624826", "Pride and Prejudice", "Jane Austen", 2010, 285, new BigDecimal(4.50),
				"Pride and Prejudice is a novel by Jane Austen that satirizes issues of marriage and social class.");
		Book updatedBook = new Book("9780141439587", "Emma", "Jane Austen", 2003, 512, new BigDecimal(5.99),
				"Clever, rich and beautiful, she sees no need for marriage, but loves interfering in the romantic lives of others, until her matchmaking plans unravel, with consequences that she never expected.");

		when(bookRepository.findById(id)).thenReturn(Optional.of(book));
		when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

		mockMvc.perform(put("/api/books/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedBook))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(updatedBook.getId()))
				.andExpect(jsonPath("$.isbn").value(updatedBook.getIsbn()))
				.andExpect(jsonPath("$.title").value(updatedBook.getTitle()))
				.andExpect(jsonPath("$.author").value(updatedBook.getAuthor()))
				.andExpect(jsonPath("$.published_year").value(updatedBook.getPublished_year()))
				.andExpect(jsonPath("$.page_count").value(updatedBook.getPage_count()))
				.andExpect(jsonPath("$.retail_price").value(updatedBook.getRetail_price()))
				.andExpect(jsonPath("$.description").value(updatedBook.getDescription())).andDo(print());
	}

	// Attempt to update a book that does not exist and check the status is 'not
	// found'
	@Test
	void returnIdNotFoundUpdateBook() throws Exception {
		logger.info("Attempt to update a book that does not exist and check the status is 'not found'");
		long id = 1L;

		Book updatedBook = new Book("9780141439587", "Emma", "Jane Austen", 2003, 512, new BigDecimal(5.99),
				"Clever, rich and beautiful, she sees no need for marriage, but loves interfering in the romantic lives of others, until her matchmaking plans unravel, with consequences that she never expected.");

		when(bookRepository.findById(id)).thenReturn(Optional.empty());
		when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

		mockMvc.perform(put("/api/books/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedBook))).andExpect(status().isNotFound()).andDo(print());
	}

	// Check DELETE works as intended for a specific id
	@Test
	void deleteBook() throws Exception {
		logger.info("Check DELETE works as intended for a specific id");
		long id = 1L;

		doNothing().when(bookRepository).deleteById(id);
		mockMvc.perform(delete("/api/books/{id}", id)).andExpect(status().isNoContent()).andDo(print());
	}

	// Check DELETE works as intended for all books
	@Test
	void deleteAllBooks() throws Exception {
		logger.info("Check DELETE works as intended for all books");
		doNothing().when(bookRepository).deleteAll();
		mockMvc.perform(delete("/api/books")).andExpect(status().isNoContent()).andDo(print());
	}
}
