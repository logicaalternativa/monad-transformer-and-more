/**
 * 
 */
package com.logicaalternativa.monadtransformerandmore.business.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scala.concurrent.Future;

import com.logicaalternativa.monadtransformerandmore.bean.Author;
import com.logicaalternativa.monadtransformerandmore.bean.Book;
import com.logicaalternativa.monadtransformerandmore.bean.Chapter;
import com.logicaalternativa.monadtransformerandmore.bean.Sales;
import com.logicaalternativa.monadtransformerandmore.bean.Summary;
import com.logicaalternativa.monadtransformerandmore.business.SrvSummaryF;
import com.logicaalternativa.monadtransformerandmore.business.impl.SrvSummaryContainerImpl;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.monad.MonadContainer;
import com.logicaalternativa.monadtransformerandmore.monad.impl.MonadContainerError;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceAuthorContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceBookContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceChapterContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceSalesContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceAuthorContainerMock;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceBookContainerMock;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceChapterContainerMock;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceSalesContainerMock;

public class SrvSummarySContainerTest {

	private  SrvSummaryF<Error,Container> srvSummary;

	private final ServiceBookContainer<Error> srvBook = new ServiceBookContainerMock();
	private final ServiceSalesContainer<Error> srvSales = new ServiceSalesContainerMock();
	private final ServiceChapterContainer<Error> srvChapter = new ServiceChapterContainerMock();
	private final ServiceAuthorContainer<Error> srvAuthor = new ServiceAuthorContainerMock();
	private final MonadContainer<Error> m = new MonadContainerError();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		srvSummary = SrvSummarySContError.dsl()
						.withSrvBook( idBook -> srvBook.getBook( idBook ) )
						.withSrvSales(s -> srvSales.getSales( s ) )
						.withSrvChapter( chapter -> srvChapter.getChapter( chapter ) )
						.build( idAuthor ->  srvAuthor.getAuthor( idAuthor  ) ) 
						;

		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void happyPath() {
		
		// Given
		final Integer bookId = 1;
		
		final Book expectedBook = srvBook.getBook(bookId).getValue();

		final List<Chapter> expectedChapters = expectedBook.getChapters().stream()
				.map(s -> srvChapter.getChapter(s).getValue())
				.collect(Collectors.toList());
		
		final Sales expectedSales = srvSales.getSales(bookId).getValue() ;
		
		final Author expectedAuthor = srvAuthor.getAuthor( expectedBook.getIdAuthor() ).getValue();

		// When
		@SuppressWarnings("unchecked")
		final Container<Error, Summary> summaryC = srvSummary
				.getSummary(bookId);

		// Then
		final Summary summary = summaryC.getValue();

		assertEquals( expectedBook, summary.getBook() );
		assertEquals( Optional.of(expectedSales), summary.getSales().get() );
		assertEquals( expectedAuthor, summary.getAuthor() );
		assertEquals( expectedChapters, summary.getChapter());

	}
	


	@Test
	public void happyPathWOSales() {
		
		// Given
		final Integer bookId = 1000;
		
		final Book expectedBook = srvBook.getBook(bookId).getValue();

		final List<Chapter> expectedChapters = expectedBook.getChapters().stream()
				.map(s -> srvChapter.getChapter(s).getValue())
				.collect(Collectors.toList());
		
		final Author expectedAuthor = srvAuthor.getAuthor( expectedBook.getIdAuthor() ).getValue();

		// When
		@SuppressWarnings("unchecked")
		final Container<Error, Summary> summaryC = srvSummary
				.getSummary(bookId);

		// Then
		final Summary summary = summaryC.getValue();

		assertEquals( expectedBook, summary.getBook() );
		assertEquals( false, summary.getSales().isPresent() );
		assertEquals( expectedAuthor, summary.getAuthor() );
		assertEquals( expectedChapters, summary.getChapter());

	}
	
	@Test
	public void errorBook() {
		
		testErrorGeneric( -1 );
		
	}
	
	@Test
	public void errorAuthor() {
		
		testErrorGeneric( 2 );
				
		
	}
	
	@Test
	public void errorChapter() {
		
		testErrorGeneric( 3 );
				
		
	}
	
	private void testErrorGeneric( final Integer bookId ) {
		
		// When
		@SuppressWarnings("unchecked")
		final Container<Error, Summary> summaryC = srvSummary
				.getSummary(bookId);

		// Then
		assertEquals( "It is impossible to get book summary", summaryC.getError().getDescription() );
		
		
	}

}
