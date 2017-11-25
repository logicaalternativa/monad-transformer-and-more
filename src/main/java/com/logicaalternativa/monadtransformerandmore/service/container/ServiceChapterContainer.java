package com.logicaalternativa.monadtransformerandmore.service.container;

import com.logicaalternativa.monadtransformerandmore.bean.Chapter;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.service.ServiceChapterF;

public interface ServiceChapterContainer<E> extends ServiceChapterF<E, Container>{
	
	Container<E, Chapter> getChapter( long idChapter );

}
