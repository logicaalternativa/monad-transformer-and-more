package com.logicaalternativa.monadtransformandmore.service.future.impl;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformandmore.bean.Author;
import com.logicaalternativa.monadtransformandmore.errors.Error;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceAuthorContainer;
import com.logicaalternativa.monadtransformandmore.service.container.impl.ServiceAuthorContainerMock;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceAuthorFutEither;



public class ServiceAuthorFutEitherMock extends ServiceFutEitherBase implements ServiceAuthorFutEither<Error>{

	final ServiceAuthorContainer<Error> srv = new ServiceAuthorContainerMock();
	
	@Override
	public Future<Either<Error, Author>> getAuthor(String id) {		
		
		return createFuture( () -> srv.getAuthor( id ) );
		
		
	}
  
    
	
  
}
