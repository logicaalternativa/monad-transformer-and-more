package com.logicaalternativa.monadtransformandmore.business.impl;

import static com.logicaalternativa.monadtransformandmore.util.Util.$_notYetImpl;
import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformandmore.bean.Summary;
import com.logicaalternativa.monadtransformandmore.business.SrvSummaryFutureEither;
import com.logicaalternativa.monadtransformandmore.errors.Error;
import com.logicaalternativa.monadtransformandmore.errors.impl.MyError;
import com.logicaalternativa.monadtransformandmore.monad.MonadContainer;
import com.logicaalternativa.monadtransformandmore.monad.MonadFutEither;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceAuthorFutEither;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceBookFutEither;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceChapterFutEither;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceSalesFutEither;

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
