package com.logicaalternativa.monadtransformerandmore
package business
package impl

import monad._
import bean._
import errors._
import service._
import monad.container._

import monad.syntax.Implicits._
import MonadContainerErrorS.ContainerError

trait WithSrvBook {
     def withSrvBook ( newsrvBook : ServiceBookF[Error,ContainerError] ) : WithSrvSales
}

trait WithSrvSales {
     def withSrvSales ( newsrvSales : ServiceSalesF[Error,ContainerError] ) : WithSrvChapter
}

trait WithSrvChapter {
     def withSrvChapter ( newsrvChapter : ServiceChapterF[Error,ContainerError] )  : WithSrvAuthor
}

trait WithSrvAuthor {
     def build ( newSrvAuthor : ServiceAuthorF[Error,ContainerError] ) : SrvSummarySContError
}

class SrvSumContainerErrorDSL(
    private val srvBook    : Option[ServiceBookF[Error,ContainerError]],
    private val srvSales   : Option[ServiceSalesF[Error,ContainerError]],
    private val srvChapter : Option[ServiceChapterF[Error,ContainerError]],
    private val srvAuthor  : Option[ServiceAuthorF[Error,ContainerError]] ) 
    extends WithSrvBook with WithSrvSales with WithSrvChapter with WithSrvAuthor {
  
    def withSrvBook ( newsrvBook : ServiceBookF[Error,ContainerError] ) : WithSrvSales = {
       new SrvSumContainerErrorDSL( Some(newsrvBook),srvSales, srvChapter, srvAuthor )
    }
    
    def withSrvSales ( newsrvSales : ServiceSalesF[Error,ContainerError] ) : WithSrvChapter= {
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
