package com.logicaalternativa.monadtransformerandmore.business.impl;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import com.logicaalternativa.monadtransformerandmore.bean.Summary;
import com.logicaalternativa.monadtransformerandmore.business.SrvSummaryContainer;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.monad.MonadContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceAuthorContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceBookContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceChapterContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceSalesContainer;

public class SrvSummaryContainerImpl<E> implements SrvSummaryContainer<E> {
	
	private final ServiceBookContainer<E> srvBook;
	private final ServiceSalesContainer<E> srvSales;
	private final ServiceChapterContainer<E> srvChapter;
	private final ServiceAuthorContainer<E> srvAuthor;
	
	private final MonadContainer<E> m;
	

	public SrvSummaryContainerImpl(ServiceBookContainer<E> srvBook,
			ServiceSalesContainer<E> srvSales,
			ServiceChapterContainer<E> srvChapter,
			ServiceAuthorContainer<E> srvAuthor, MonadContainer<E> m) {
		super();
		this.srvBook = srvBook;
		this.srvSales = srvSales;
		this.srvChapter = srvChapter;
		this.srvAuthor = srvAuthor;
		this.m = m;
	}



	@Override
	public Container<E, Summary> getSummary(Integer idBook) {
		
		return $_notYetImpl();
		
	}

}
