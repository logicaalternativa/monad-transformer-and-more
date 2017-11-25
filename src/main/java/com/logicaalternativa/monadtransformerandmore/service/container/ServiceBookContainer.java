package com.logicaalternativa.monadtransformerandmore.service.container;

import com.logicaalternativa.monadtransformerandmore.bean.Book;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.service.ServiceBookF;


public interface ServiceBookContainer<E>  extends ServiceBookF<E, Container>{
	
	Container<E, Book> getBook( int bookId );

}
