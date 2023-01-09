package com.frog.frogbackendprogrammingtest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.frog.frogbackendprogrammingtest.datamodel.Book;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;
    
    Logger logger = Logger.getLogger(BookRepositoryTest.class.getName());

    // Test for findByIsbn()
    @Test
    public void findBookByIsbn() {
    	logger.info("Test for findByIsbn()");
        // Add book
        Book book = new Book("9781847624826", "Pride and Prejudice", "Jane Austen", 2010, 285, new BigDecimal(4.50), "Pride and Prejudice is a novel by Jane Austen that satirizes issues of marriage and social class.");
        entityManager.persist(book);
        entityManager.flush();	// flush so data persists for the whole test

        Optional<Book> found = bookRepository.findByIsbn(book.getIsbn());
        assertThat(found.get().getIsbn())
          .isEqualTo(book.getIsbn());
    }
    
    // Test for findByTitle()
    @Test
    public void findBooksByTitle() {
    	logger.info("Test for findByTitle()");
        // Add books
    	List<Book> books = new ArrayList<>(
    	        Arrays.asList(new Book("9781847624826", "Pride and Prejudice", "Jane Austen", 2010, 285, new BigDecimal(4.50), "Pride and Prejudice is a novel by Jane Austen that satirizes issues of marriage and social class."),
    	            new Book("9780099419785", "To Kill A Mockingbird", "Harper Lee", 1989, 320, new BigDecimal(8.99), "To Kill a Mockingbird views the brutality of racism in the Deep South through the eyes of the big-hearted child narrator, Scout."),
    	            new Book("9780141187761", "Nineteen Eighty-Four", "George Orwell", 2004, 384, new BigDecimal(8.99), "Who controls the past controls the future: who controls the present controls the past.")));
        books.forEach(entityManager::persist); // use forEach as books are added from an ArrayList
        entityManager.flush();

        List<Book> found = bookRepository.findByTitle(books.get(1).getTitle());
        System.out.print(found.toString());
        assertThat(found.get(0).getTitle())
          .isEqualTo(books.get(1).getTitle());
    }
    
    // Test for existsByIsbn()
    @Test
    public void checkIfBookExistsByIsbn() {
    	logger.info("Test for existsByIsbn()");
        // Add book
        Book book = new Book("9781847624826", "Pride and Prejudice", "Jane Austen", 2010, 285, new BigDecimal(4.50), "Pride and Prejudice is a novel by Jane Austen that satirizes issues of marriage and social class.");
        entityManager.persist(book);
        entityManager.flush();
        
        boolean exists = bookRepository.existsByIsbn(book.getIsbn());
        assertThat(exists)
          .isEqualTo(book.getIsbn().equals(bookRepository.findByIsbn(book.getIsbn()).get().getIsbn()));
    }
}
