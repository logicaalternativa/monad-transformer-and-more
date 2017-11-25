package com.logicaalternativa.monadtransformerandmore
package business

import monad._
import bean._
import service._

import monad.syntax.Implicits._

trait SrvSummaryF[E,P[_]] {
  
  implicit val E : Monad[E,P]
  
  val srvBook : ServiceBookF[E,P]
  val srvSales : ServiceSalesF[E,P]
  val srvChapter : ServiceChapterF[E,P]
  val srvAuthor : ServiceAuthorF[E,P]
  
  def getSummary( idBook: Int) : P[Summary] = {
    
     ???
    
  } 
  
}
