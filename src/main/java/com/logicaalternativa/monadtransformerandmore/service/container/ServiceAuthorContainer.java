package com.logicaalternativa.monadtransformerandmore.service.container;

import com.logicaalternativa.monadtransformerandmore.bean.Author;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.service.ServiceAuthorF;

public interface ServiceAuthorContainer<E> extends ServiceAuthorF<E,Container> {
	
	Container<E, Author> getAuthor( String id );

}
