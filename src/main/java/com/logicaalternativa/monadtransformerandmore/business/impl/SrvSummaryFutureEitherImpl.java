package com.logicaalternativa.monadtransformerandmore.business.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import scala.Tuple2;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.Promise;
import scala.util.Either;
import scala.util.Either.LeftProjection;
import scala.util.Left;
import scala.util.Right;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;


import com.logicaalternativa.monadtransformerandmore.bean.Author;
import com.logicaalternativa.monadtransformerandmore.bean.Book;
import com.logicaalternativa.monadtransformerandmore.bean.Chapter;
import com.logicaalternativa.monadtransformerandmore.bean.Sales;
import com.logicaalternativa.monadtransformerandmore.bean.Summary;
import com.logicaalternativa.monadtransformerandmore.business.SrvSummaryFutureEither;
import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.monad.MonadFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceAuthorFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceBookFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceChapterFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceSalesFutEither;
import com.logicaalternativa.monadtransformerandmore.util.Java8;


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
		this.m = m;
	}

	@Override
	public Future<Either<Error, Summary>> getSummary(Integer idBook) {
		
		final ExecutionContext ec = ExecutionContexts.global();
		
		final Future<Either<Error, Book>> bookF = srvBook.getBook( idBook );		
		final Future<Either<Error, Sales>> salesF = srvSales.getSales( idBook );
		
		final Future<Tuple2<Either<Error, Book>, Either<Error, Sales>>> bookAndSalesF = bookF.zip(salesF);
		
		
		final Future<Either<Error, Author>> authorF = bookF.flatMap(
				eitherBook -> {
					
					if( eitherBook.isRight() ) {
						
						return srvAuthor.getAuthor( eitherBook.right().get().getIdAuthor() );
						
						
					} else {
						
						Error error = eitherBook.left().get();						
						
						return Futures.successful( new Left<>(error)   );
					}
					
					
									
				}
				, ec);
		
		
		final Future<Iterable<Either<Error, Chapter>>> iterableFutureChapter = bookF.flatMap(
				eitherBook -> {
					
					final List<Long> chapters = eitherBook.right().get().getChapters();
					
					final List<Future<Either<Error, Chapter>>> chapterListFuture = chapters.stream().map(
						
						idChapter -> srvChapter.getChapter(idChapter)
						
						).collect(Collectors.toList());
					
					
					return Futures.sequence(chapterListFuture, ec);
					
				} , ec);
		
		final Future<Either<Error, Summary>> res = bookAndSalesF
				
				.flatMap(
						
						bookAndSales -> {
						
							return iterableFutureChapter.flatMap(
									
								iterableChapter -> {
									
									
									return authorF.map(
												
												authorE -> {
													
													final List<Chapter> chapter = StreamSupport.stream(iterableChapter.spliterator(), true)
														.map( s -> s.right().get() )
														.collect(Collectors.toList() )
														;													
													
													final Book book = bookAndSales._1.right().get();
													final Optional<Sales> sales = Optional.of( bookAndSales._2().right().get() ) ;
													final Author author = authorE.right().get();
													
													return new Right<> ( new Summary(book, chapter, sales, author) );
													
												}, ec);
								
								
								
								} , ec);
						
						}
						
				,ec); 						
		return res;
	}

}
