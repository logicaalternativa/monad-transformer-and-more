package com.logicaalternativa.monadtransformerandmore
package monad


import collection.JavaConverters._

trait Monad[E, P[_]] {
  
  def pure[T]( value : T ) : P[T]
  
  def flatMap[A,T]( from : P[A], f : (A) => P[T] ) : P[T]
  
  def raiseError[T] ( error: E ) : P[T]

  def recoverWith[T]( from : P[T], f : (E) => P[T] ) : P[T]

  /**
   * Derived
   */
  
  def map[A,T]( from : P[A], f : (A) => T ) : P[T] = {
    
      flatMap[A,T]( from, a => pure( f(a) ) ) 
    
    }
  
  def recover[T]( from : P[T], f : (E) => T ) : P[T] = {
    
      recoverWith( from, e  => pure( f(e) ) )
    
  }

  def flatten[T]( from : P[P[T]] ) : P[T] = {
    
      flatMap( from, ( t : P[T] ) => t )
    
  }

  def flatMap2[A,B,T]( fromA : P[A], fromB :P[B], f : (A,B) => P[T] ) : P[T] = flatten( map2( fromA, fromB, f ) )
  
  def map2[A,B,T]( fromA : P[A], fromB : P[B], f : (A,B) => T ) : P[T] = {
    
      flatMap[A,T](
        fromA,
        a => map[B,T](
          fromB,
          b => f( a, b)
        )
      )
    
  }
  
  def flatMap3[A,B,C,T]( fromA : P[A], fromB : P[B], fromC :P[C], f : (A,B,C) => P[T] ) : P[T] = flatten( map3(fromA, fromB, fromC, f ) )
  
  def map3[A,B,C,T]( fromA : P[A], fromB :P[B], fromC :P[C], f : (A,B,C) => T ) : P[T] = {
    
      flatMap[A,T](
        fromA,
        a => map2[B,C,T] (
              fromB,
              fromC,
              ( b,c) => f( a, b, c)
        
            )
      
      )
  }
  
  def sequence[T]( l : List[P[T]]  ) : P[List[T]] =  {
    
      l match {
          case Nil => pure( List() )
          case head :: tail => map2[T, List[T], List[T]](
                                  head,
                                  sequence( tail ),
                                  //~ ( h, l ) => h +: l
                                  ( h, l ) =>  l.+:( h )
                                )
      }
    
    
  }
  
}
