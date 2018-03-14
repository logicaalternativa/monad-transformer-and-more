package com.logicaalternativa.monadtransformerandmore.service.container.impl;

import java.util.Arrays;
import java.util.List;

import com.logicaalternativa.monadtransformerandmore.bean.Book;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.errors.impl.MyError;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceBookContainer;
import com.logicaalternativa.monadtransformerandmore.errors.Error;



public class ServiceBookContainerMock implements ServiceBookContainer<Error>{
  
    @Override
    public Container<Error, Book> getBook( int bookId ) {
      
       if ( bookId < 0 ) { 
        return Container.error( new MyError("Book not found " + bookId) );
      }
       
      if ( bookId == 4 ) { 
    	  
        throw new RuntimeException( "Exception to get Book" );
        
      }
      
      final String nameBook = "Book " + bookId;
      final String idAuthor = "author-book-" + bookId;      
      
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
