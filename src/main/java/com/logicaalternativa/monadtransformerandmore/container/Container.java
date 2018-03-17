package com.logicaalternativa.monadtransformerandmore.container;


public final class Container<E,T> {
	
	final T value;
	
	final Boolean ok;
	
	final E error;
	
	public T getValue() {
		return value;
	}

	public Boolean isOk() {
		return ok;
	}

	public E getError() {
		return error;
	}

	private Container(final T value, final E error, boolean ok) {
		this.value = value;
		this.error = error;
		this.ok = ok;
	}

	public static <E, T> Container<E, T> error( E error ) {
		
		return new Container<E, T>(null, error, false );		
		
	}
	
	public static <E, T> Container<E, T> value( T value ) {
		
		return new Container<E, T>(value, null, true );		
		
	}

	@Override
	public String toString() {
		return "Container [value=" + value + ", ok=" + ok + ", error=" + error
				+ "]";
	}
	

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((error == null) ? 0 : error.hashCode());
		result = prime * result + ((ok == null) ? 0 : ok.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Container other = (Container) obj;
		if (error == null) {
			if (other.error != null) {
				return false;
			}
		} else if (!error.equals(other.error)) {
			return false;
		}
		if (ok == null) {
			if (other.ok != null) {
				return false;
			}
		} else if (!ok.equals(other.ok)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}
	
}
