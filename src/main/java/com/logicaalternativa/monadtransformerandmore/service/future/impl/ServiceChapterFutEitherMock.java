package com.logicaalternativa.monadtransformerandmore.service.future.impl;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Chapter;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceChapterContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceChapterContainerMock;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceChapterFutEither;

import com.logicaalternativa.monadtransformerandmore.errors.Error;

public class ServiceChapterFutEitherMock extends ServiceFutEitherBase implements ServiceChapterFutEither<Error> {
	
	final ServiceChapterContainer<Error> srv = new ServiceChapterContainerMock();

	@Override
	public Future<Either<Error, Chapter>> getChapter(Long idChapter) {
		
		return createFuture( () -> srv.getChapter( idChapter ) );
		
	}
	

}
