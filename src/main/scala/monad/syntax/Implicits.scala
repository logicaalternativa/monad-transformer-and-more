package com.logicaalternativa.monadtransformerandmore
package monad
package syntax

import com.logicaalternativa.monadtransformerandmore.function.{Function3 => JFunction3}
import java.util.function.{Function => JFunction, BiFunction => JFunction2}
import java.util.{List => JList}

import collection.JavaConverters._

object Implicits {  
  
  class WrapperMonad[E,P[_],A]( val value : P[A], private val E : Monad[E,P] ) {

    def flatMap[T]( f : A => P[T] ) : P[T] = E.flatMap( value, f )
    
    def >>= [T]( f : A => P[T] ) : P[T] = flatMap( f )
    
    def map[T]( f : A => T ) : P[T] =  E.map( value, f )
    
    def recover( f : E => A ) : P[A] = E.recover( value, f )
    
    def recoverWith( f : E => P[A]) : P[A] = E.recoverWith( value, f )
    
    def |@| [B] ( b: P[B] ) = new ApplicativeBuilder2( value, b, E )
    
  }
  
  class ApplicativeBuilder2[E,P[_],A,B]( private val valueA : P[A], private val valueB : P[B], private val E : Monad[E,P] ) {
    
      def apply[T]( f : (A,B) => T ) : P[T] = E.map2( valueA, valueB, f)
      
      def |@| [C] ( c: P[C] )  = new ApplicativeBuilder3( valueA, valueB, c, E )
    
  }
  
  class ApplicativeBuilder3[E,P[_],A,B,C]( private val valueA : P[A], private val valueB : P[B], private val valueC : P[C], private val E : Monad[E,P] ) {
    
      def apply[T]( f : (A,B,C) => T ) : P[T] = E.map3( valueA, valueB, valueC, f)
    
  }
  
  implicit def toWrapMonad[E,T,P[_]]( s : P[T]  )( implicit E : Monad[E,P] ) = new WrapperMonad( s, E ) 
  
  implicit def unWrapMonad[E,T,P[_]]( s : WrapperMonad[E,P,T]  ) : P[T] = s.value
    
}
