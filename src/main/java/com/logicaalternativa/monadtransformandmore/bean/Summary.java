package com.logicaalternativa.monadtransformandmore.bean;

import java.util.List;
import java.util.Optional;

public class Summary {
	
	private final Book book;
	
	private final List<Chapter> chapter;
	
	private final Optional<Sales> sales;
	
	private final Author author;

	public Summary(Book book, List<Chapter> chapter, Optional<Sales> sales, Author author) {
		super();
		this.book = book;
		this.chapter = chapter;
		this.sales = sales;
		this.author = author;
	}

	public Book getBook() {
		return book;
	}

	public List<Chapter> getChapter() {
		return chapter;
	}

	public Optional<Sales> getSales() {
		return sales;
	}

	public Author getAuthor() {
		return author;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((book == null) ? 0 : book.hashCode());
		result = prime * result + ((chapter == null) ? 0 : chapter.hashCode());
		result = prime * result + ((sales == null) ? 0 : sales.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Summary other = (Summary) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (book == null) {
			if (other.book != null)
				return false;
		} else if (!book.equals(other.book))
			return false;
		if (chapter == null) {
			if (other.chapter != null)
				return false;
		} else if (!chapter.equals(other.chapter))
			return false;
		if (sales == null) {
			if (other.sales != null)
				return false;
		} else if (!sales.equals(other.sales))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Summary [book=" + book + ", chapter=" + chapter + ", sales="
				+ sales + ", author=" + author + "]";
	}
	
	
	

}
