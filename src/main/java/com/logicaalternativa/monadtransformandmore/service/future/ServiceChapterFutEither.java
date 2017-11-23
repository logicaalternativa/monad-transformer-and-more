package com.logicaalternativa.monadtransformandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformandmore.bean.Chapter;

public interface ServiceChapterFutEither<E> {
	
	Future<Either<E, Chapter>> getChapter( Long idChapter );

}
