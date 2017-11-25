package com.logicaalternativa.monadtransformerandmore
package business
package impl

import monad._
import bean._
import errors._
import service._
import monad.futeither._

import monad.syntax.Implicits._

import scala.concurrent.ExecutionContext

import MonadFutEitherS.FutEitherError

object SrvSummarySFutError {
  
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
  
}
