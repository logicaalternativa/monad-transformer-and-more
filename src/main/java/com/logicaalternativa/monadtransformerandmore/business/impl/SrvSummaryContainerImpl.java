package com.logicaalternativa.monadtransformerandmore.business.impl;

import static com.logicaalternativa.monadtransformerandmore.monad.MonadContainerWrapper.wrap;
import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Author;
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
import com.logicaalternativa.monadtransformerandmore.errors.impl.MyError;

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
		
		final Container<Error, Optional<Sales>> salesFut = wrap( srvSales.getSales( idBook ), m )
				 .map(  sales -> Optional.of( sales ) )
				 .recover( e -> Optional.empty() )
				 .value()
				;		
				
				return wrap(srvBook.getBook(idBook), m)
				.flatMap( book -> { 
									final Container<Error, Summary> map3 = m.map3(
							
										srvAuthor.getAuthor( book.getIdAuthor() ),
										m.sequence ( 
												book.getChapters()
												.stream()
												.map( ch -> srvChapter.getChapter(ch) )
												.collect(Collectors.toList() )
										),
										salesFut,
										  (author,chapters, salesO) -> new Summary(book, chapters , salesO, author)
										);
									
									return map3;
						}
				)
				.recoverWith( e -> m.raiseError(  new MyError( "It is impossible to get book summary" ) ))
				.value();
		
	}

}
