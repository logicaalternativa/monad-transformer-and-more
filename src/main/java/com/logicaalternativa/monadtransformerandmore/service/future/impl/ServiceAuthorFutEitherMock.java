package com.logicaalternativa.monadtransformerandmore.service.future.impl;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Author;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceAuthorContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceAuthorContainerMock;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceAuthorFutEither;

import com.logicaalternativa.monadtransformerandmore.errors.Error;

public class ServiceAuthorFutEitherMock extends ServiceFutEitherBase implements ServiceAuthorFutEither<Error>{

	final ServiceAuthorContainer<Error> srv = new ServiceAuthorContainerMock();
	
	@Override
	public Future<Either<Error, Author>> getAuthor(String id) {		
		
		return createFuture( () -> srv.getAuthor( id ) );
		
		
	}
  
    
	
  
}
