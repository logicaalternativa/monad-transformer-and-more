/**
 * 
 */
package com.logicaalternativa.monadtransformerandmore.business.impl;

import static com.logicaalternativa.monadtransformerandmore.business.impl.SrvSummarySFutError.dsl;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scala.concurrent.Await;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.util.Either;
import akka.dispatch.ExecutionContexts;

import com.logicaalternativa.monadtransformerandmore.bean.Author;
import com.logicaalternativa.monadtransformerandmore.bean.Book;
import com.logicaalternativa.monadtransformerandmore.bean.Chapter;
import com.logicaalternativa.monadtransformerandmore.bean.Sales;
import com.logicaalternativa.monadtransformerandmore.bean.Summary;
import com.logicaalternativa.monadtransformerandmore.business.SrvSummaryF;
import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceAuthorContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceBookContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceChapterContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceSalesContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceAuthorContainerMock;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceBookContainerMock;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceChapterContainerMock;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceSalesContainerMock;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceAuthorFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceBookFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceChapterFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceSalesFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.impl.ServiceAuthorFutEitherMock;
import com.logicaalternativa.monadtransformerandmore.service.future.impl.ServiceBookFutEitherMock;
import com.logicaalternativa.monadtransformerandmore.service.future.impl.ServiceChapterFutEitherMock;
import com.logicaalternativa.monadtransformerandmore.service.future.impl.ServiceSalesFutEitherMock;

public class SrvSummarySFutErrorTest {

	@SuppressWarnings("rawtypes")
	private SrvSummaryF<Error,Future> srvSummary;

	private final ServiceBookFutEither<Error> srvBook = new ServiceBookFutEitherMock();
	private final ServiceSalesFutEither<Error> srvSales = new ServiceSalesFutEitherMock();
	private final ServiceChapterFutEither<Error> srvChapter = new ServiceChapterFutEitherMock();
	private final ServiceAuthorFutEither<Error> srvAuthor = new ServiceAuthorFutEitherMock();
	
	private final ServiceChapterContainer<Error> srvChapterCheck = new ServiceChapterContainerMock();
	private final ServiceBookContainer<Error> srvBookCheck = new ServiceBookContainerMock();
	private final ServiceSalesContainer<Error> srvSalesCheck = new ServiceSalesContainerMock();
	private final ServiceAuthorContainer<Error> srvAuthorCheck = new ServiceAuthorContainerMock();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		final ExecutionContextExecutor ec = ExecutionContexts.global();		
		
		// srvSummary = SrvSummarySFutError.apply(srvBook, srvSales, srvChapter, srvAuthor, ec);
		
		srvSummary = dsl(ec)
				.withSrvBook( idBook -> srvBook.getBook( idBook ) )
				.withSrvSales(sales -> srvSales.getSales( sales ) )
				.withSrvChapter( chapter -> srvChapter.getChapter( chapter ) )
				.build( idAuthor ->  srvAuthor.getAuthor( idAuthor )) 
				;
		
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		final Future summaryFu = srvSummary.getSummary(bookId);
		
		final Either<Error, Summary> res = (Either<Error, Summary>) Await.result(summaryFu, Duration.apply(100, TimeUnit.MILLISECONDS));

		// Then
		final Summary summary = res.right().get();

		assertEquals( expectedBook, summary.getBook() );
		assertEquals( Optional.of(expectedSales), summary.getSales() );
		assertEquals( expectedAuthor, summary.getAuthor() );
		assertEquals( expectedChapters, summary.getChapter());

	}
	


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void happyPathWOSales() throws Exception {
		
		// Given
		final Integer bookId = 1000;
		
		final Book expectedBook = srvBookCheck.getBook(bookId).getValue();

		final List<Chapter> expectedChapters = expectedBook.getChapters().stream()
				.map(s -> srvChapterCheck.getChapter(s).getValue())
				.collect(Collectors.toList());
		
		final Author expectedAuthor = srvAuthorCheck.getAuthor( expectedBook.getIdAuthor() ).getValue();

		// When
		final Future summaryFu = srvSummary.getSummary(bookId);
		
		final Either<Error, Summary> res = (Either<Error, Summary>) Await.result(summaryFu, Duration.apply(100, TimeUnit.MILLISECONDS));

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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void testErrorGeneric( final Integer bookId ) throws Exception {
		
		// When
		final Future summaryFu = srvSummary
				.getSummary(bookId);
		
		final Either<Error, Summary> res = (Either<Error, Summary>) Await.result(summaryFu, Duration.apply(100, TimeUnit.MILLISECONDS));


		// Then
		assertEquals( "It is impossible to get book summary", res.left().get().getDescription() );
		
		
	}

}
