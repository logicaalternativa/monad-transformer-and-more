package com.logicaalternativa.monadtransformerandmore.errors.impl;
import com.logicaalternativa.monadtransformerandmore.errors.Error;

public class MyError implements Error{
	
	private final String description;

	public MyError(String description) {
		super();
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "MyError [description=" + description + "]";
	}

}
