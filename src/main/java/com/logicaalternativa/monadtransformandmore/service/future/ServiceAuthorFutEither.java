package com.logicaalternativa.monadtransformandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformandmore.bean.Author;
public interface ServiceAuthorFutEither<E> {
	
	Future<Either<E, Author>> getAuthor( String id );

}
