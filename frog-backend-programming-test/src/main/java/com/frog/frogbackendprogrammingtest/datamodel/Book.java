package com.frog.frogbackendprogrammingtest.datamodel;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) // Automatically increases primary key id when a new entry is added
	private long id;
	
	@Column(unique = true, name = "ISBN") // Set as unique so as there should not be duplicates
	private String isbn;

	@Column(name = "title")
	private String title;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "published") // Year of publication
	private Integer published_year;
	
	@Column(name = "pages") // Number of pages in a book
	private Integer page_count;
	
	@Column(name = "price")
	private BigDecimal retail_price;	
	
	@Column(name = "description")
	private String description;
	
	public Book() {
	}
	
	public Book(String isbn, String title, String author, Integer published_year, Integer page_count, BigDecimal retail_price, String description) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.published_year = published_year;
		this.page_count = page_count;
		this.retail_price = retail_price;
		this.description = description;
	}
	
	// For testing
	public Book(long id, String isbn, String title, String author, Integer published_year, Integer page_count, BigDecimal retail_price, String description) {
		this.id = id;
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.published_year = published_year;
		this.page_count = page_count;
		this.retail_price = retail_price;
		this.description = description;
	}


	public long getId() {
		return id;
	}

	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getPublished_year() {
		return published_year;
	}

	public void setPublished_year(Integer published_year) {
		this.published_year = published_year;
	}

	public Integer getPage_count() {
		return page_count;
	}

	public void setPage_count(Integer page_count) {
		this.page_count = page_count;
	}

	public BigDecimal getRetail_price() {
		return retail_price;
	}

	public void setRetail_price(BigDecimal retail_price) {
		this.retail_price = retail_price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	// override toString() method for use in tests
	public String toString() {
		return "ID = " + id + ", ISBN = " + isbn + ", title = " + title + 
				", author = " + author + ", published year = " + published_year + 
				", page count = " + page_count + ", retail price = " + retail_price + 
				", description = " + description;
	}
	
}
