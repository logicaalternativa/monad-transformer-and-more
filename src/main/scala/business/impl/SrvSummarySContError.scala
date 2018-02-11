package com.logicaalternativa.monadtransformerandmore
package business
package impl

import monad._
import bean._
import errors._
import errors.impl._
import service._
import monad.container._

import monad.syntax.Implicits._
import MonadContainerErrorS.ContainerError


object SrvSummarySContError {
    
 def dsl : WithSrvBook[Error, ContainerError] = SrvSumContainerErrorDSL(None,None,None,None)
  
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
  
  override def getGenericError( s : String ) = new MyError( s ) 
  
}


object SrvSumContainerErrorDSL{
    
    def apply( srvBook    : Option[ServiceBookF[Error,ContainerError]]   ,
                srvSales   : Option[ServiceSalesF[Error,ContainerError]]  ,
                srvChapter : Option[ServiceChapterF[Error,ContainerError]],
                srvAuthor  : Option[ServiceAuthorF[Error,ContainerError]]   
            ) = new SrvSumContainerErrorDSL( srvBook, srvSales, srvChapter, srvAuthor )
    
}

class SrvSumContainerErrorDSL(
    val srvBook    : Option[ServiceBookF[Error,ContainerError]],
    val srvSales   : Option[ServiceSalesF[Error,ContainerError]],
    val srvChapter : Option[ServiceChapterF[Error,ContainerError]],
    val srvAuthor  : Option[ServiceAuthorF[Error,ContainerError]] ) 
    extends SrvSummaryFDSL[Error,ContainerError] {
  
    def build ( newSrvAuthor : ServiceAuthorF[Error,ContainerError]  ) : SrvSummarySContError = {
        SrvSummarySContError( srvBook.get, srvSales.get, srvChapter.get, newSrvAuthor )
    }
    
    protected def copy( srvBook    : Option[ServiceBookF[Error,ContainerError]]   ,
                        srvSales   : Option[ServiceSalesF[Error,ContainerError]]  ,
                        srvChapter : Option[ServiceChapterF[Error,ContainerError]],
                        srvAuthor  : Option[ServiceAuthorF[Error,ContainerError]]   
                    ) : SrvSummaryFDSL[Error,ContainerError] = {
        
        SrvSumContainerErrorDSL( srvBook, srvSales, srvChapter, srvAuthor )
    }
        
}

