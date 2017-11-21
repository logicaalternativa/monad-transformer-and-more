package com.logicaalternativa.monadtransformandmore.service.container;

import com.logicaalternativa.monadtransformandmore.bean.Book;
import com.logicaalternativa.monadtransformandmore.container.Container;

public interface ServiceBookContainer<E> {
	
	Container<E, Book> getBook( Integer bookId );

}
