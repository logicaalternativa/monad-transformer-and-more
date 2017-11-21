package com.logicaalternativa.monadtransformandmore.container;


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
	
}