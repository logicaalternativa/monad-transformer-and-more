package com.logicaalternativa.monadtransformerandmore
package business
package impl

import monad._
import bean._
import errors._
import errors.impl._
import service._
import monad.futeither._

import monad.syntax.Implicits._

import scala.concurrent.ExecutionContext

import MonadFutEitherS.FutEitherError

object SrvSummarySFutError {
  
 def dsl( ec: ExecutionContext ) = SrvSummarySFutErrorDSL( None, None, None, None, ec )
  
 def apply( 
    implicit srvBook : ServiceBookF[Error,FutEitherError],
    srvSales : ServiceSalesF[Error,FutEitherError],
    srvChapter : ServiceChapterF[Error,FutEitherError],
    srvAuthor : ServiceAuthorF[Error,FutEitherError],
    ec: ExecutionContext )  = new SrvSummarySFutError
  
}

class SrvSummarySFutError( 
    implicit val srvBook : ServiceBookF[Error,FutEitherError],
    val srvSales : ServiceSalesF[Error,FutEitherError],
    val srvChapter : ServiceChapterF[Error,FutEitherError],
    val srvAuthor : ServiceAuthorF[Error,FutEitherError],
    val ec: ExecutionContext ) extends SrvSummaryF[Error,FutEitherError] {
 
  implicit val E  = MonadFutEitherS( ec )  
  override def getGenericError( s : String ) = new MyError( s ) 
  
}

object SrvSummarySFutErrorDSL{ 
  
    
    def apply( srvBook    : Option[ServiceBookF[Error,FutEitherError]],
               srvSales   : Option[ServiceSalesF[Error,FutEitherError]],
               srvChapter : Option[ServiceChapterF[Error,FutEitherError]],
               srvAuthor  : Option[ServiceAuthorF[Error,FutEitherError]],
               ec: ExecutionContext ) = new SrvSummarySFutErrorDSL( srvBook, srvSales, srvChapter, srvAuthor, ec )
}



class SrvSummarySFutErrorDSL(
    val srvBook    : Option[ServiceBookF[Error,FutEitherError]],
    val srvSales   : Option[ServiceSalesF[Error,FutEitherError]],
    val srvChapter : Option[ServiceChapterF[Error,FutEitherError]],
    val srvAuthor  : Option[ServiceAuthorF[Error,FutEitherError]],
    val ec: ExecutionContext ) 
    extends SrvSummaryFDSL[Error,FutEitherError] {
  
    def build ( newSrvAuthor : ServiceAuthorF[Error,FutEitherError]  ) : SrvSummarySFutError = {
        SrvSummarySFutError( srvBook.get, srvSales.get, srvChapter.get, newSrvAuthor, ec )
    }
    
    protected def copy( srvBook    : Option[ServiceBookF[Error,FutEitherError]]   ,
                        srvSales   : Option[ServiceSalesF[Error,FutEitherError]]  ,
                        srvChapter : Option[ServiceChapterF[Error,FutEitherError]],
                        srvAuthor  : Option[ServiceAuthorF[Error,FutEitherError]]   
                    ) : SrvSummaryFDSL[Error,FutEitherError] = {
        
        SrvSummarySFutErrorDSL( srvBook, srvSales, srvChapter, srvAuthor, ec )
    }
        
}





