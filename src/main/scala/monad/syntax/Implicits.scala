package com.logicaalternativa.monadtransformerandmore
package monad
package syntax

import com.logicaalternativa.monadtransformerandmore.function.{Function3 => JFunction3}
import java.util.function.{Function => JFunction, BiFunction => JFunction2}
import java.util.{List => JList}

import collection.JavaConverters._

object Implicits {  
  
  class WrapperMonad[E,P[_],A]( val value : P[A], private val E : Monad[E,P] ) {

    def flatMap[T]( from : A => P[T] ) : P[T] = {
      
      E.flatMap( value, from )
        
    } 
    
    def map[T]( from : A => T ) : P[T] = {
      
      E.map( value, from )
        
    } 
    
    def recover( f : E => A ) : P[A] = { 
      
      E.recover( value, f )
      
    }

    def recoverWith( f : E => P[A]) : P[A] = { 
      
      E.recoverWith( value, f )
      
    }
    
  } 
  
  
  implicit def toWrapMonad[E,T,P[_]]( s : P[T]  )( implicit E : Monad[E,P] ) = new WrapperMonad( s, E ) 
  
  implicit def unWrapMonad[E,T,P[_]]( s : WrapperMonad[E,P,T]  ) : P[T] = s.value
  
  
}
