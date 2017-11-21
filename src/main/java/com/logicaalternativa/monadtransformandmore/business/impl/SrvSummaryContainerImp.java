package com.logicaalternativa.monadtransformandmore.business.impl;

import com.logicaalternativa.monadtransformandmore.bean.Summary;
import com.logicaalternativa.monadtransformandmore.business.SrvSummaryContainer;
import com.logicaalternativa.monadtransformandmore.container.Container;
import com.logicaalternativa.monadtransformandmore.monad.MonadContainer;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceAuthorContainer;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceBookContainer;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceChapterContainer;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceSalesContainer;

import static com.logicaalternativa.monadtransformandmore.util.Util.$_notYetImpl;

public class SrvSummaryContainerImp<E> implements SrvSummaryContainer<E> {
	
	private final ServiceBookContainer<E> srvBook;
	private final ServiceSalesContainer<E> srvSales;
	private final ServiceChapterContainer<E> srvChapter;
	private final ServiceAuthorContainer<E> srvAuthor;
	
	private final MonadContainer<E> m;
	

	public SrvSummaryContainerImp(ServiceBookContainer<E> srvBook,
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
