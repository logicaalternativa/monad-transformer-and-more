package com.logicaalternativa.monadtransformerandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Chapter;

public interface ServiceChapterFutEither<E> {
	
	Future<Either<E, Chapter>> getChapter( Long idChapter );

}
