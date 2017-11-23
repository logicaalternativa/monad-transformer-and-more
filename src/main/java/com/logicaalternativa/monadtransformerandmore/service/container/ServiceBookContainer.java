package com.logicaalternativa.monadtransformerandmore.service.container;

import com.logicaalternativa.monadtransformerandmore.bean.Book;
import com.logicaalternativa.monadtransformerandmore.container.Container;


public interface ServiceBookContainer<E> {
	
	Container<E, Book> getBook( Integer bookId );

}
