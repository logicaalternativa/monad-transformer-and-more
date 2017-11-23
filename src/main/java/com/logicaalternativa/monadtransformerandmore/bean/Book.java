package com.logicaalternativa.monadtransformerandmore.bean;

import java.util.List;

public class Book {
	
	private final String nameBook;
	private final String idAuthor;
	private final List<Long> chapters;
	
	public Book(String nameBook, String idAuthor, List<Long> chapters) {
		super();
		this.nameBook = nameBook;
		this.idAuthor = idAuthor;
		this.chapters = chapters;
	}
	public String getNameBook() {
		return nameBook;
	}
	public String getIdAuthor() {
		return idAuthor;
	}
	public List<Long> getChapters() {
		return chapters;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chapters == null) ? 0 : chapters.hashCode());
		result = prime * result
				+ ((idAuthor == null) ? 0 : idAuthor.hashCode());
		result = prime * result
				+ ((nameBook == null) ? 0 : nameBook.hashCode());
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
		Book other = (Book) obj;
		if (chapters == null) {
			if (other.chapters != null)
				return false;
		} else if (!chapters.equals(other.chapters))
			return false;
		if (idAuthor == null) {
			if (other.idAuthor != null)
				return false;
		} else if (!idAuthor.equals(other.idAuthor))
			return false;
		if (nameBook == null) {
			if (other.nameBook != null)
				return false;
		} else if (!nameBook.equals(other.nameBook))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Book [nameBook=");
		builder.append(nameBook);
		builder.append(", idAuthor=");
		builder.append(idAuthor);
		builder.append(", chapters=");
		builder.append(chapters);
		builder.append("]");
		return builder.toString();
	}
	
	

}
