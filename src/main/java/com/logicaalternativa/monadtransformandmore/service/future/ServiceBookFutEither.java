package com.logicaalternativa.monadtransformandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformandmore.bean.Book;

public interface ServiceBookFutEither<E> {
	
	Future<Either<E, Book>> getBook( Integer bookId );

}
