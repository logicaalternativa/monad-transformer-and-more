package com.logicaalternativa.monadtransformerandmore.service.container;

import com.logicaalternativa.monadtransformerandmore.bean.Chapter;
import com.logicaalternativa.monadtransformerandmore.container.Container;

public interface ServiceChapterContainer<E> {
	
	Container<E, Chapter> getChapter( Long idChapter );

}
