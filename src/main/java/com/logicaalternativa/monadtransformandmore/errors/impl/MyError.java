package com.logicaalternativa.monadtransformandmore.errors.impl;

import com.logicaalternativa.monadtransformandmore.errors.Error;

public class MyError implements Error {
	
	private final String description;

	public MyError(String description) {
		super();
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
