package com.logicaalternativa.monadtransformerandmore
package monad
package syntax

import com.logicaalternativa.monadtransformerandmore.function.{Function3 => JFunction3}
import java.util.function.{Function => JFunction, BiFunction => JFunction2}
import java.util.{List => JList}

object Implicits {  
  
  class WrapperMonad[E,P[_],A]( val value : P[A], private val E : Monad[E,P] ) {

    def flatMap[T]( from : A => P[T] ) : P[T] = {
      
      E.flatMap( value, from )
        
    } 
    
    def map[T]( from : A => T ) : P[T] = {
      
      E.map( value, from )
        
    } 
    
  }
    
  implicit def funToScala[A,T](  f :  Function[A,T] ) : JFunction[A,T]  = {
    
    new JFunction[A,T] {
      
        def apply( s : A ) = f( s )
        
    }
         
  }
  
  implicit def fun2ToScala[A,B,T](  f :  Function2[A,B,T] ) : JFunction2[A,B,T]  = {
    
    new JFunction2[A,B,T] {
      
        def apply( a : A, b : B ) = f( a, b )
        
    }
         
  }  
  
  
  implicit def fun3ToScala[A,B,C,T](  f :  Function3[A,B,C,T] ) : JFunction3[A,B,C,T]  = {
    
    new JFunction3[A,B,C,T] {
      
        def apply( a : A, b : B, c: C) = f( a, b, c )
        
    }
         
  }
  
  implicit def toWrapMonad[E,T,P[_]]( s : P[T]  )( implicit E : Monad[E,P] ) = new WrapperMonad( s, E ) 
  
  implicit def unWrapMonad[E,T,P[_]]( s : WrapperMonad[E,P,T]  ) : P[T] = s.value
  
  implicit def asScalaList[T]( l : JList[T] ) : List[T] = l.asScala.toList
  
  implicit def asJavaList[T]( l : List[T] ) : JList[T] = l.asJava
  
}
