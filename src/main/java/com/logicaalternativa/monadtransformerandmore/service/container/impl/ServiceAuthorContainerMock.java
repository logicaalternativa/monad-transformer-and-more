package com.logicaalternativa.monadtransformerandmore.service.container.impl;

import com.logicaalternativa.monadtransformerandmore.bean.Author;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.errors.impl.MyError;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceAuthorContainer;
import com.logicaalternativa.monadtransformerandmore.errors.Error;

public class ServiceAuthorContainerMock implements ServiceAuthorContainer<Error>{
  
    
	@Override
	public Container<Error, Author> getAuthor(String id) {
		
		if ( "author-book-2".equals( id )  ){
			
			return Container.error( new MyError( "Author not found " + id ));
			
		}
		
		final Author author = new Author( "Name of " + id ) ;
		
		return Container.value( author );
	}
  
  }
