package com.logicaalternativa.monadtransformandmore.service.container;

import com.logicaalternativa.monadtransformandmore.bean.Author;
import com.logicaalternativa.monadtransformandmore.container.Container;
public interface ServiceAuthorContainer<E> {
	
	Container<E, Author> getAuthor( String id );

}
