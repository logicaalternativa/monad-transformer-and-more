package com.logicaalternativa.monadtransformandmore.service.container.impl;

import com.logicaalternativa.monadtransformandmore.bean.Author;
import com.logicaalternativa.monadtransformandmore.container.Container;
import com.logicaalternativa.monadtransformandmore.errors.Error;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceAuthorContainer;


public class ServiceAuthorContainerMock implements ServiceAuthorContainer<Error>{
  
    
	@Override
	public Container<Error, Author> getAuthor(String id) {
		
		final Author author = new Author( "Name of " + id ) ;
		
		return Container.value( author );
	}
  
  }
