package com.logicaalternativa.monadtransformerandmore.service.container;

import com.logicaalternativa.monadtransformerandmore.bean.Author;
import com.logicaalternativa.monadtransformerandmore.container.Container;
public interface ServiceAuthorContainer<E> {
	
	Container<E, Author> getAuthor( String id );

}
