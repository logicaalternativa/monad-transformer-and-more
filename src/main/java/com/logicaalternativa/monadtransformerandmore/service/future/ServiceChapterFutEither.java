package com.logicaalternativa.monadtransformerandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Chapter;
import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.service.ServiceChapterF;

public interface ServiceChapterFutEither<E> extends ServiceChapterF<Error, Future>{
	
	Future<Either<E, Chapter>> getChapter( long idChapter );

}
