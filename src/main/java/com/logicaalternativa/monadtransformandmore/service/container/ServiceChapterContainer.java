package com.logicaalternativa.monadtransformandmore.service.container;

import com.logicaalternativa.monadtransformandmore.bean.Chapter;
import com.logicaalternativa.monadtransformandmore.container.Container;

public interface ServiceChapterContainer<E> {
	
	Container<E, Chapter> getChapter( Long idChapter );

}
