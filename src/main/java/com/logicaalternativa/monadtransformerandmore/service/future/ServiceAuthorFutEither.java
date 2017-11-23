package com.logicaalternativa.monadtransformerandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Author;
public interface ServiceAuthorFutEither<E> {
	
	Future<Either<E, Author>> getAuthor( String id );

}
