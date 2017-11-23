package com.logicaalternativa.monadtransformerandmore.bean;

public class Chapter {
	
	private final String title;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Chapter [title=");
		builder.append(title);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Chapter other = (Chapter) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public String getTitle() {
		return title;
	}

	public Chapter(String title) {
		super();
		this.title = title;
	}

}
