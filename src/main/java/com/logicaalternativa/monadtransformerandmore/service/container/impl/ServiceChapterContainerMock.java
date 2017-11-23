package com.logicaalternativa.monadtransformerandmore.service.container.impl;

import com.logicaalternativa.monadtransformerandmore.bean.Chapter;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.errors.impl.MyError;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceChapterContainer;

public class ServiceChapterContainerMock implements ServiceChapterContainer<Error> {

	@Override
	public Container<Error, Chapter> getChapter(Long idChapter) {
		
		if ( idChapter.equals(3005L) ) {
			
			return Container.error( new MyError( "Chapter not found " + idChapter ) );
			
		}
		
		final Chapter chapter =  new Chapter( "Title chapter - " + idChapter );
		
		return Container.value( chapter );
	}

}
