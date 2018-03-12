package com.logicaalternativa.monadtransformerandmore.business.impl;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import com.logicaalternativa.monadtransformerandmore.bean.Book;
import com.logicaalternativa.monadtransformerandmore.bean.Chapter;
import com.logicaalternativa.monadtransformerandmore.bean.Sales;
import com.logicaalternativa.monadtransformerandmore.bean.Summary;
import com.logicaalternativa.monadtransformerandmore.business.SrvSummaryContainer;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.monad.MonadContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceAuthorContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceBookContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceChapterContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceSalesContainer;
import com.logicaalternativa.monadtransformerandmore.errors.Error;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SrvSummaryContainerImpl implements SrvSummaryContainer<Error> {
	
	private final ServiceBookContainer<Error> srvBook;
	private final ServiceSalesContainer<Error> srvSales;
	private final ServiceChapterContainer<Error> srvChapter;
	private final ServiceAuthorContainer<Error> srvAuthor;
	
	private final MonadContainer<Error> m;
	

	public SrvSummaryContainerImpl(ServiceBookContainer<Error> srvBook,
			ServiceSalesContainer<Error> srvSales,
			ServiceChapterContainer<Error> srvChapter,
			ServiceAuthorContainer<Error> srvAuthor, MonadContainer<Error> m) {
		super();
		this.srvBook = srvBook;
		this.srvSales = srvSales;
		this.srvChapter = srvChapter;
		this.srvAuthor = srvAuthor;
		this.m = m;
	}



	@Override
	public Container<Error, Summary> getSummary(Integer idBook) {

		Container<Error, Book> book = this.srvBook.getBook(idBook);
		Book value = book.getValue();

		List<Chapter> chapters = value.getChapters().stream()
				.map(idChapter -> this.srvChapter.getChapter(idChapter))
				.map(container -> container.getValue())
				.collect(Collectors.toList());

		Sales sales = this.srvSales.getSales(idBook).getValue();


		Summary summary = new Summary(value, chapters, Optional.of(sales), this.srvAuthor.getAuthor(value.getIdAuthor()).getValue());

		return Container.value(summary);
		
	}

}
