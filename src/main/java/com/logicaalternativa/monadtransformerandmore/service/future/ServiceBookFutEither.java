package com.logicaalternativa.monadtransformerandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Book;
import com.logicaalternativa.monadtransformerandmore.service.ServiceBookF;

public interface ServiceBookFutEither<E>  extends ServiceBookF<E,Future>{
	
	Future<Either<E, Book>> getBook( int bookId );

}
