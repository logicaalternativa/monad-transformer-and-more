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
                  
                  
    val res = bookP >>= {
        
        book => {
          
           val authorP  = srvAuthor.getAuthor( book.getIdAuthor )
    
           val listP = sequence( 
                    book.getChapters.asScala.toList.map{
                      ch => srvChapter.getChapter( ch )
                    }
                  )
                  
           ( listP |@| salesP |@| authorP ) {
             
              ( listC, sales, author ) => new Summary( book, listC.asJava, sales, author )
             
             
            }
          
          
        }
       
       
    }
                
    //~ val res = for {
        //~ 
        //~ sales  <- salesP  
        //~ book   <- bookP
        //~ summary  <- createSummary( book, sales )      
        //~ 
    //~ } yield( summary )
    
    res.recoverWith {
      _ =>  raiseError( getGenericError( "It is impossible to get book summary" ) ) 
    }
  } 
  
  private def createSummary( book: Book, sales : Optional[Sales] ) : P[Summary] = {
    
    val authorP  = srvAuthor.getAuthor( book.getIdAuthor )
    
    val listP = sequence( 
                    book.getChapters.asScala.toList.map{
                      ch => srvChapter.getChapter( ch )
                    }
                  )
     for {
       
       author <- authorP
       listC <- listP
              
      }  yield( new Summary( book, listC.asJava, sales, author ) )            
                  
    
    
  }
  
  protected[SrvSummaryF] def getGenericError( s : String ) : E
  
}
