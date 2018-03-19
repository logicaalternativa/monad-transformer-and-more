package com.logicaalternativa.monadtransformerandmore
package monad
package futeither

import errors._
import errors.impl._
import scala.concurrent._
import scala.util._

import scala.concurrent.ExecutionContext

import MonadFutEitherS.FutEitherError

object MonadFutEitherS {
  
    // import scala.concurrent.ExecutionContext.Implicits.global
  
    type FutEitherError[T] =  Future[Either[Error,T]]    
    
    def apply( implicit ec: ExecutionContext ) = new MonadFutEitherS
  
}

class MonadFutEitherS(implicit ec : ExecutionContext ) extends Monad[Error, FutEitherError] {
  
  def pure[T]( value : T ) : FutEitherError[T] = Future {
                                                  Right( value )
                                                }
  
  def flatMap[A,T]( from : FutEitherError[A], f : (A) => FutEitherError[T] ) : FutEitherError[T] = {
    
      from
      .flatMap {
          case Right( value ) => f( value )
          case Left( error ) => raiseError( error )
          
      }.recoverWith( recoverFunction )
    
    
  }
  
  def raiseError[T] ( error: Error ) : FutEitherError[T] = Future {
                                                  Left( error )
                                                }

  def recoverWith[T]( from : FutEitherError[T], f : (Error) => FutEitherError[T] ) : FutEitherError[T] = {
    
      from
       .flatMap {
           case Right( value ) => pure( value )
           case Left( error ) => f( error )
       }.recoverWith( recoverFunction )
    
    
  }
  
  
  private def recoverFunction[T] : PartialFunction[Throwable,FutEitherError[T]] = {
    
    case t : Throwable => raiseError( new MyError( t.getMessage ) ) 
    
  }
 
  
}
