package com.logicaalternativa.monadtransformerandmore
package business

import monad._
import bean._
import service._

import monad.syntax.Implicits._

import java.util.Optional
import scala.collection.JavaConverters._

trait SrvSummaryF[E,P[_]] {
  
  implicit val E : Monad[E,P]
  
  import E._
  
  val srvBook : ServiceBookF[E,P]
  val srvSales : ServiceSalesF[E,P]
  val srvChapter : ServiceChapterF[E,P]
  val srvAuthor : ServiceAuthorF[E,P]
  
  def getSummary( idBook: Int) : P[Summary] = {
    
    
    val bookP = srvBook.getBook( idBook ) 
    val salesP = srvSales.getSales( idBook )
                  .map( Optional.of(_) )
                  .recover( _ => Optional.empty[Sales] )
                
    
    
    for{
        
        sales  <- salesP  
        book   <- bookP
        author <- srvAuthor.getAuthor( book.getIdAuthor )
        listC  <- sequence( 
                    book.getChapters.asScala.toList.map{
                      ch => srvChapter.getChapter( ch )
                    }
                  )
      
    } yield( new Summary( book, listC.asJava, sales, author ) )
    
    
    //~ srvBook.getBook( idBook ).flatMap( (book : Book) => ??? )
    
    
    
    ???
    
  } 
  
  protected[SrvSummaryF] def getGenericError( s : String ) : E
  
}
