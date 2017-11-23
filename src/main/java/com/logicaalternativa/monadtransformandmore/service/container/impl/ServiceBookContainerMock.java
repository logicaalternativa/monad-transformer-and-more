package com.logicaalternativa.monadtransformandmore.service.container.impl;

import java.util.Arrays;
import java.util.List;

import com.logicaalternativa.monadtransformandmore.bean.Book;
import com.logicaalternativa.monadtransformandmore.container.Container;
import com.logicaalternativa.monadtransformandmore.errors.Error;
import com.logicaalternativa.monadtransformandmore.errors.impl.MyError;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceBookContainer;


public class ServiceBookContainerMock implements ServiceBookContainer<Error>{
  
    @Override
    public Container<Error, Book> getBook( Integer bookId ) {
    	
      if ( bookId < 0 ) {    	  
    	  return Container.error( new MyError("Book not found " + bookId) );
      }
      
      final String nameBook = "Book " + bookId;
      final String idAuthor = "author-book- " + bookId;      
      
      final Long base =  bookId * 1000L;      
      final List<Long> chapters  = Arrays.asList( 
                                       base + 1L, 
                                       base + 2L, 
                                       base + 3L,
                                       base + 4L,
                                       base + 5L
                                    ) ;
      
      final Book book =  new Book( nameBook, idAuthor, chapters );
      
      return Container.value( book );
      
    }
  
  }
