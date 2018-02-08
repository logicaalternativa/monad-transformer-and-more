package com.logicaalternativa.monadtransformerandmore
package business

import monad._
import bean._
import errors._
import service._

import monad.syntax.Implicits._
import MonadContainerErrorS.ContainerError

trait WithSrvBook[E, P[_]] {
     def withSrvBook ( newsrvBook : ServiceBookF[E,P] ) : WithSrvSales
}

trait WithSrvSales[E, P[_]] {
     def withSrvSales ( newsrvSales : ServiceSalesF[E,P] ) : WithSrvChapter
}

trait WithSrvChapter[E, P[_]] {
     def withSrvChapter ( newsrvChapter : ServiceChapterF[E,P] )  : WithSrvAuthor
}

trait WithSrvAuthor[E, P[_]] {
     def build ( newSrvAuthor : ServiceAuthorF[E,P] ) : SrvSummarySContError
}

class SrvSummaryFDSL[E,P[_]](
    private val srvBook    : Option[ServiceBookF[E,P]],
    private val srvSales   : Option[ServiceSalesF[E,P]],
    private val srvChapter : Option[ServiceChapterF[E,P]],
    private val srvAuthor  : Option[ServiceAuthorF[E,P]] ) 
    extends WithSrvBook[E,P] with WithSrvSales[E,P] with WithSrvChapter[E,P] with WithSrvAuthor[E,P] {
  
    def withSrvBook ( newsrvBook : ServiceBookF[E,P] ) : WithSrvSales[E,P] = {
       new SrvSumContainerErrorDSL( Some(newsrvBook),srvSales, srvChapter, srvAuthor )
    }
    
    def withSrvSales ( newsrvSales : ServiceSalesF[E,P] ) : WithSrvChapter= {
        new SrvSumContainerErrorDSL( srvBook, Some(newsrvSales), srvChapter, srvAuthor )
    }
    
    def withSrvChapter ( newsrvChapter : ServiceChapterF[Error,ContainerError] ) : WithSrvAuthor= {
        new SrvSumContainerErrorDSL( srvBook, srvSales, Some(newsrvChapter), srvAuthor )
    }
    
    def build ( newSrvAuthor : ServiceAuthorF[Error,ContainerError]  ) : SrvSummarySContError = {
        SrvSummarySContError( srvBook.get, srvSales.get, srvChapter.get, newSrvAuthor )
    }
        
}

object SrvSummarySContError {
    
 def dsl : WithSrvBook = new SrvSumContainerErrorDSL(None,None,None,None)
  
 def apply( 
    implicit srvBook : ServiceBookF[Error,ContainerError],
    srvSales : ServiceSalesF[Error,ContainerError],
    srvChapter : ServiceChapterF[Error,ContainerError],
    srvAuthor : ServiceAuthorF[Error,ContainerError] )  = new SrvSummarySContError
  
}

class SrvSummarySContError( 
    implicit val srvBook : ServiceBookF[Error,ContainerError],
    val srvSales : ServiceSalesF[Error,ContainerError],
    val srvChapter : ServiceChapterF[Error,ContainerError],
    val srvAuthor : ServiceAuthorF[Error,ContainerError]) extends SrvSummaryF[Error,ContainerError] {
 
  implicit val E  = MonadContainerErrorS()   
  
}
