package com.logicaalternativa.monadtransformerandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Author;
import com.logicaalternativa.monadtransformerandmore.service.ServiceAuthorF;
public interface ServiceAuthorFutEither<E> extends ServiceAuthorF<E, Future>{
	
	Future<Either<E, Author>> getAuthor( String id );

}
