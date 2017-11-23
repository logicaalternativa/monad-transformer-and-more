package com.logicaalternativa.monadtransformandmore.service.future.impl;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformandmore.bean.Book;
import com.logicaalternativa.monadtransformandmore.errors.Error;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceBookContainer;
import com.logicaalternativa.monadtransformandmore.service.container.impl.ServiceBookContainerMock;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceBookFutEither;


public class ServiceBookFutEitherMock extends ServiceFutEitherBase implements ServiceBookFutEither<Error>{
	
	final ServiceBookContainer<Error> srv = new ServiceBookContainerMock();

	@Override
	public Future<Either<Error, Book>> getBook( final Integer bookId) {
		
		return createFuture( () -> srv.getBook( bookId ) );
	}
  
    
  
  }
