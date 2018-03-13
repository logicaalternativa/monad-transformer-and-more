package com.logicaalternativa.monadtransformerandmore.business.impl;

import com.logicaalternativa.monadtransformerandmore.bean.*;
import scala.Tuple2;
import scala.concurrent.Future;
import scala.concurrent.Promise;
import scala.util.Either;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;

import com.logicaalternativa.monadtransformerandmore.business.SrvSummaryFutureEither;
import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.monad.MonadFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceAuthorFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceBookFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceChapterFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceSalesFutEither;
import com.logicaalternativa.monadtransformerandmore.util.Java8;
import scala.util.Right;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.logicaalternativa.monadtransformerandmore.util.Java8.*;
import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

public class SrvSummaryFutureEitherImpl implements SrvSummaryFutureEither<Error> {

	private final ServiceBookFutEither<Error> srvBook;
	private final ServiceSalesFutEither<Error> srvSales;
	private final ServiceChapterFutEither<Error> srvChapter;
	private final ServiceAuthorFutEither<Error> srvAuthor;
	
	private final MonadFutEither<Error> m;
	
	
	public SrvSummaryFutureEitherImpl(ServiceBookFutEither<Error> srvBook,
			ServiceSalesFutEither<Error> srvSales,
			ServiceChapterFutEither<Error> srvChapter,
			ServiceAuthorFutEither<Error> srvAuthor,
			MonadFutEither<Error> m) {
		super();
		this.srvBook = srvBook;
		this.srvSales = srvSales;
		this.srvChapter = srvChapter;
		this.srvAuthor = srvAuthor;
		this.m= m;
	}

	@Override
	public Future<Either<Error, Summary>> getSummary(Integer idBook) {

		/*
		zip esta en el api de Scala para combinar futuros.
		Se puede hacer tambien con flatMap y map: F[A].flatMap( a -> F[B].map( b -> f(a,b)) )
		 */

		Future<Either<Error, Book>> bookF = this.srvBook.getBook(idBook);
		Future<Either<Error, Sales>> salesF = this.srvSales.getSales(idBook);

		Future<Tuple2<Either<Error, Book>, Either<Error, Sales>>> zip = bookF.zip(salesF);


		Future<Either<Error, Summary>> summaryF = zip.flatMap(tuple -> {
			final Book book = tuple._1.right().get();
			final Sales sales = tuple._2.right().get();

			Future<Iterable<Either<Error, Chapter>>> chapters = Futures.sequence(
					book
							.getChapters()
							.stream()
							.map(chapterId -> this.srvChapter.getChapter(chapterId))
							.collect(Collectors.toList()), ExecutionContexts.global());


			Future<Either<Error, Author>> author = this.srvAuthor.getAuthor(book.getIdAuthor());

			return chapters.zip(author).flatMap(tuple2 -> {

				List<Chapter> chaptersList = new ArrayList<>();
				tuple2._1.iterator().forEachRemaining(chapter -> chaptersList.add(chapter.right().get()));

				Summary summary = new Summary(book, chaptersList, Optional.of(sales), tuple2._2.right().get());

				return Futures.future(() -> {
					return new Right(summary);
				}, ExecutionContexts.global());
			}, ExecutionContexts.global());

		}, ExecutionContexts.global());

		return summaryF;
	}

}
