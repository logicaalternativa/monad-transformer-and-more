package com.logicaalternativa.monadtransformandmore.service.container.impl;

import com.logicaalternativa.monadtransformandmore.bean.Chapter;
import com.logicaalternativa.monadtransformandmore.container.Container;
import com.logicaalternativa.monadtransformandmore.errors.Error;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceChapterContainer;

public class ServiceChapterContainerMock implements ServiceChapterContainer<Error> {

	@Override
	public Container<Error, Chapter> getChapter(Long idChapter) {
		
		final Chapter chapter =  new Chapter( "Title chapter - " + idChapter );
		
		return Container.value( chapter );
	}

}
