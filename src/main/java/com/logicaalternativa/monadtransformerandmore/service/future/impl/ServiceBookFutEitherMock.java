package com.logicaalternativa.monadtransformerandmore.service.future.impl;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Book;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceBookContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceBookContainerMock;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceBookFutEither;

import com.logicaalternativa.monadtransformerandmore.errors.Error;
public class ServiceBookFutEitherMock extends ServiceFutEitherBase implements ServiceBookFutEither<Error>{
	
	final ServiceBookContainer<Error> srv = new ServiceBookContainerMock();

	@Override
	public Future<Either<Error, Book>> getBook( final int bookId) {
		
		return createFuture( () -> srv.getBook( bookId ) );
	}
  
    
  
  }
