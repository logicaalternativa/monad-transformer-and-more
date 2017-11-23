package com.logicaalternativa.monadtransformandmore.service.future.impl;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformandmore.bean.Chapter;
import com.logicaalternativa.monadtransformandmore.errors.Error;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceChapterContainer;
import com.logicaalternativa.monadtransformandmore.service.container.impl.ServiceChapterContainerMock;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceChapterFutEither;

public class ServiceChapterFutEitherMock extends ServiceFutEitherBase implements ServiceChapterFutEither<Error> {
	
	final ServiceChapterContainer<Error> srv = new ServiceChapterContainerMock();

	@Override
	public Future<Either<Error, Chapter>> getChapter(Long idChapter) {
		
		return createFuture( () -> srv.getChapter( idChapter ) );
		
	}
	

}
