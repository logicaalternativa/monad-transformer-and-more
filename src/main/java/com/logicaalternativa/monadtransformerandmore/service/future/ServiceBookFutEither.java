package com.logicaalternativa.monadtransformerandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Book;

public interface ServiceBookFutEither<E> {
	
	Future<Either<E, Book>> getBook( Integer bookId );

}
