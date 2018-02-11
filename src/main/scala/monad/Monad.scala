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
  
  def map[A,T]( from : P[A], f : (A) => T ) : P[T] = ???
  
  def recover[T]( from : P[T], f : (E) => T ) : P[T] = ???

  def flatten[T]( from : P[P[T]] ) : P[T] = ???

  def flatMap2[A,B,T]( fromA : P[A], fromB :P[B], f : (A,B) => P[T] ) : P[T] = ???
  
  def map2[A,B,T]( fromA : P[A], fromB : P[B], f : (A,B) => T ) : P[T] = ???
  
  def flatMap3[A,B,C,T]( fromA : P[A], fromB : P[B], fromC :P[C], f : (A,B,C) => P[T] ) : P[T] = ???
  
  def map3[A,B,C,T]( fromA : P[A], fromB :P[B], fromC :P[C], f : (A,B,C) => T ) : P[T] = ???
  
  def sequence[T]( l : List[P[T]]  ) : P[List[T]] =  ???
  
}
