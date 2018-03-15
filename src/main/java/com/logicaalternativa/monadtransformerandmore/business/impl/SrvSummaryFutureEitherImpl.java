package com.logicaalternativa.monadtransformerandmore.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.logicaalternativa.monadtransformerandmore.errors.impl.MyError;
import com.logicaalternativa.monadtransformerandmore.monad.MonadFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceAuthorFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceBookFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceChapterFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceSalesFutEither;
import com.logicaalternativa.monadtransformerandmore.util.Java8;
import com.sun.glass.ui.View.Capability;

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
		
		
		final Future<Either<Error, Book>> bookF = srvBook.getBook( idBook ).recover(
				recoverF( e -> new Left<>( new MyError( e.getMessage() ) ) )
				,ec);		
		
		final Future<Either<Error, Sales>> salesF = srvSales.getSales( idBook );
		
		final Future<Tuple2<Either<Error, Book>, Either<Error, Sales>>> bookAndSalesF = bookF.zip(salesF);
		
		
		final Future<Either<Error, Author>> authorF = bookF.flatMap(
				eitherBook -> getAuthor(eitherBook)
				, ec)
				.recover(
					recoverF( e -> new Left<>( new MyError( e.getMessage() ) ) )
					,ec);
		
		
		final Future<Either<Error, List<Chapter>>> iterableFutureChapter = bookF.flatMap(
				eitherBook ->  getChapters(ec, eitherBook) 
				, ec);
		
		final Future<Either<Error, Summary>> res = bookAndSalesF
				
				.flatMap(
						
						bookAndSales ->  iterableFutureChapter.flatMap(
									
								iterableChapter -> authorF.map(
												
												authorE -> createSummary(
															bookAndSales,
															iterableChapter,
															authorE)
													
												, ec)
								
								
								
								, ec)
						
						
				,ec); 						
		return res;
	}

	/**
	 * 
	 * @param eitherBook
	 * @return
	 */
	
	private Future<Either<Error, Author>> getAuthor(
			Either<Error, Book> eitherBook) {
		
		if( eitherBook.isLeft() ) {
			
			Error error = eitherBook.left().get();						
			
			return Futures.successful( new Left<>(error)   );
			
			
			
		} else {
			
			return srvAuthor.getAuthor( eitherBook.right().get().getIdAuthor() );
		}
	}

	/**
	 * 
	 * @param ec
	 * @param eitherBook
	 * @return
	 */
	
	private Future<Either<Error, List<Chapter>>> getChapters(
			final ExecutionContext ec, Either<Error, Book> eitherBook) {
		
		if ( eitherBook.isLeft() ) {
			
			return Futures.successful( new Left<>( eitherBook.left().get()) );
			
		}
		
		
		final List<Long> chapters = eitherBook.right().get().getChapters();
		
		final List<Future<Either<Error, Chapter>>> chapterListFuture = chapters.stream().map(
			
			idChapter -> srvChapter.getChapter(idChapter)
			
			).collect(Collectors.toList());
		
	
		return Futures.sequence(chapterListFuture, ec).map(
				
				iterableChapter -> {
					
					
					final Map<Boolean, List<Either<Error, Chapter>>> groupBy = StreamSupport.stream(iterableChapter.spliterator(), true)
						.collect(Collectors.groupingBy(Either::isRight))
						;
					
					
					if ( groupBy.get(false) != null && ! groupBy.get(false).isEmpty() ) {
						
						return new Left<> ( groupBy.get(false).get(0).left().get() );
								
						
					}
					
					final List<Chapter> chapterList = groupBy.get( true )
													.stream()
													.map( s -> s.right().get() )
													.collect(Collectors.toList());
					
					return new Right<>(chapterList);
					
				}, ec);
	}

 /**
  * 
  * @param bookAndSales
  * @param iterableChapter
  * @param authorE
  * @return
  */
	
	private Either<Error, Summary> createSummary(
			Tuple2<Either<Error, Book>, Either<Error, Sales>> bookAndSales,
			Either<Error, List<Chapter>> listChapterE,
			Either<Error, Author> authorE) {
		
		
		if ( bookAndSales._1.isLeft() ) {
			
			return new Left<>( new MyError( "It is impossible to get book summary" ) );
			
		}
		
		
		final Book book = bookAndSales._1.right().get();
		
		
		if ( listChapterE.isLeft() ) {
			
			return new Left<>( new MyError( "It is impossible to get book summary" ) );
			
		}
		
		
		final List<Chapter> chapter = listChapterE.right().get();
		
		
		
		final Optional<Sales> sales = bookAndSales._2().isLeft() 
										? Optional.empty()
												:Optional.of( bookAndSales._2().right().get() ) ;
		
		if ( authorE.isLeft() ) {
			
			return new Left<>( new MyError( "It is impossible to get book summary" ) );
			
		}
		
		final Author author = authorE.right().get();
		
		return new Right<> ( new Summary(book, chapter, sales, author) );
	}

}
