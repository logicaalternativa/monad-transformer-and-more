package com.logicaalternativa.monadtransformerandmore.business.impl;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Summary;
import com.logicaalternativa.monadtransformerandmore.business.SrvSummaryFutureEither;
import com.logicaalternativa.monadtransformerandmore.monad.MonadFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceAuthorFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceBookFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceChapterFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceSalesFutEither;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

public class SrvSummaryFutureEitherImpl<E> implements SrvSummaryFutureEither<E> {

	private final ServiceBookFutEither<E> srvBook;
	private final ServiceSalesFutEither<E> srvSales;
	private final ServiceChapterFutEither<E> srvChapter;
	private final ServiceAuthorFutEither<E> srvAuthor;
	
	private final MonadFutEither<E> m;
	
	
	public SrvSummaryFutureEitherImpl(ServiceBookFutEither<E> srvBook,
			ServiceSalesFutEither<E> srvSales,
			ServiceChapterFutEither<E> srvChapter,
			ServiceAuthorFutEither<E> srvAuthor,
			MonadFutEither<E> m) {
		super();
		this.srvBook = srvBook;
		this.srvSales = srvSales;
		this.srvChapter = srvChapter;
		this.srvAuthor = srvAuthor;
		this.m= m;
	}

	@Override
	public Future<Either<E, Summary>> getSummary(Integer idBook) {
		
		return $_notYetImpl();
	}

}
