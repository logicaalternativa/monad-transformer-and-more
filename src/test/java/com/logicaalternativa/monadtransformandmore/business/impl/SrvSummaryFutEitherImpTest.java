/**
 * 
 */
package com.logicaalternativa.monadtransformandmore.business.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.util.Either;
import akka.dispatch.ExecutionContexts;

import com.logicaalternativa.monadtransformandmore.bean.Author;
import com.logicaalternativa.monadtransformandmore.bean.Book;
import com.logicaalternativa.monadtransformandmore.bean.Chapter;
import com.logicaalternativa.monadtransformandmore.bean.Sales;
import com.logicaalternativa.monadtransformandmore.bean.Summary;
import com.logicaalternativa.monadtransformandmore.business.SrvSummaryFutureEither;
import com.logicaalternativa.monadtransformandmore.errors.Error;
import com.logicaalternativa.monadtransformandmore.monad.MonadFutEither;
import com.logicaalternativa.monadtransformandmore.monad.impl.MonadFutEitherError;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceAuthorContainer;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceBookContainer;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceChapterContainer;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceSalesContainer;
import com.logicaalternativa.monadtransformandmore.service.container.impl.ServiceAuthorContainerMock;
import com.logicaalternativa.monadtransformandmore.service.container.impl.ServiceBookContainerMock;
import com.logicaalternativa.monadtransformandmore.service.container.impl.ServiceChapterContainerMock;
import com.logicaalternativa.monadtransformandmore.service.container.impl.ServiceSalesContainerMock;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceAuthorFutEither;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceBookFutEither;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceChapterFutEither;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceSalesFutEither;
import com.logicaalternativa.monadtransformandmore.service.future.impl.ServiceAuthorFutEitherMock;
import com.logicaalternativa.monadtransformandmore.service.future.impl.ServiceBookFutEitherMock;
import com.logicaalternativa.monadtransformandmore.service.future.impl.ServiceChapterFutEitherMock;
import com.logicaalternativa.monadtransformandmore.service.future.impl.ServiceSalesFutEitherMock;

public class SrvSummaryFutEitherImpTest {

	private SrvSummaryFutureEither<Error> srvSummary;

	private final ServiceBookFutEither<Error> srvBook = new ServiceBookFutEitherMock();
	private final ServiceSalesFutEither<Error> srvSales = new ServiceSalesFutEitherMock();
	private final ServiceChapterFutEither<Error> srvChapter = new ServiceChapterFutEitherMock();
	private final ServiceAuthorFutEither<Error> srvAuthor = new ServiceAuthorFutEitherMock();
	private final MonadFutEither<Error> m = new MonadFutEitherError( ExecutionContexts.global() );
	
	private final ServiceChapterContainer<Error> srvChapterCheck = new ServiceChapterContainerMock();
	private final ServiceBookContainer<Error> srvBookCheck = new ServiceBookContainerMock();
	private final ServiceSalesContainer<Error> srvSalesCheck = new ServiceSalesContainerMock();
	private final ServiceAuthorContainer<Error> srvAuthorCheck = new ServiceAuthorContainerMock();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		srvSummary = new SrvSummaryFutureEitherImpl<Error>(srvBook, srvSales,
				srvChapter, srvAuthor, m);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void happyPath() throws Exception {
		
		// Given
		final Integer bookId = 1;
		
		final Book expectedBook = srvBookCheck.getBook(bookId).getValue();

		final List<Chapter> expectedChapters = expectedBook.getChapters().stream()
				.map(s -> srvChapterCheck.getChapter(s).getValue())
				.collect(Collectors.toList());
		
		final Sales expectedSales = srvSalesCheck.getSales(bookId).getValue() ;
		
		final Author expectedAuthor = srvAuthorCheck.getAuthor( expectedBook.getIdAuthor() ).getValue();

		// When
		final Future<Either<Error, Summary>> summaryFu = srvSummary
				.getSummary(bookId);
		
		final Either<Error, Summary> res = Await.result(summaryFu, Duration.apply(100, TimeUnit.MILLISECONDS));

		// Then
		final Summary summary = res.right().get();

		assertEquals( expectedBook, summary.getBook() );
		assertEquals( Optional.of(expectedSales), summary.getSales().get() );
		assertEquals( expectedAuthor, summary.getAuthor() );
		assertEquals( expectedChapters, summary.getChapter());

	}
	


	@Test
	public void happyPathWOSales() throws Exception {
		
		// Given
		final Integer bookId = 1000;
		
		final Book expectedBook = srvBookCheck.getBook(bookId).getValue();

		final List<Chapter> expectedChapters = expectedBook.getChapters().stream()
				.map(s -> srvChapterCheck.getChapter(s).getValue())
				.collect(Collectors.toList());
		
		final Sales expectedSales = srvSalesCheck.getSales(bookId).getValue() ;
		
		final Author expectedAuthor = srvAuthorCheck.getAuthor( expectedBook.getIdAuthor() ).getValue();

		// When
		final Future<Either<Error, Summary>> summaryFu = srvSummary
				.getSummary(bookId);
		
		final Either<Error, Summary> res = Await.result(summaryFu, Duration.apply(100, TimeUnit.MILLISECONDS));

		// Then
		final Summary summary = res.right().get();

		assertEquals( expectedBook, summary.getBook() );
		assertEquals( false, summary.getSales().isPresent() );
		assertEquals( expectedAuthor, summary.getAuthor() );
		assertEquals( expectedChapters, summary.getChapter());

	}
	
	@Test
	public void errorBook() throws Exception {
		
		testErrorGeneric( -1 );
		
	}
	
	@Test
	public void errorAuthor() throws Exception {
		
		testErrorGeneric( 2 );
				
		
	}
	
	@Test
	public void errorChapter() throws Exception {
		
		testErrorGeneric( 3 );
				
		
	}
	
	private void testErrorGeneric( final Integer bookId ) throws Exception {
		
		// When
		final Future<Either<Error, Summary>> summaryFuture = srvSummary
				.getSummary(bookId);
		
		final Future<Either<Error, Summary>> summaryFu = srvSummary
				.getSummary(bookId);
		
		final Either<Error, Summary> res = Await.result(summaryFu, Duration.apply(100, TimeUnit.MILLISECONDS));


		// Then
		assertEquals( "It is impossible to get book summary", res.left().get().getDescription() );
		
		
	}

}
