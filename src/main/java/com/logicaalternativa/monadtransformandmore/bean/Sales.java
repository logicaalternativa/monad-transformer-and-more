package com.logicaalternativa.monadtransformandmore.bean;

public class Sales {
	
	private final String printed;
	private final String sales;
	
	public Sales(String printed, String sales) {
		super();
		this.printed = printed;
		this.sales = sales;
	}

	public String getPrinted() {
		return printed;
	}

	public String getSales() {
		return sales;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((printed == null) ? 0 : printed.hashCode());
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
		Sales other = (Sales) obj;
		if (printed == null) {
			if (other.printed != null)
				return false;
		} else if (!printed.equals(other.printed))
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
		StringBuilder builder = new StringBuilder();
		builder.append("Sales [printed=");
		builder.append(printed);
		builder.append(", sales=");
		builder.append(sales);
		builder.append("]");
		return builder.toString();
	}
	
	

}
