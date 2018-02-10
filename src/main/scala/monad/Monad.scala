package com.logicaalternativa.monadtransformerandmore
package monad

import function.{Function3 => JFunction3}
import java.util.function.{Function => JFunction, BiFunction => JFunction2}
import java.util.{List => JList}
import syntax.Implicits._

import collection.JavaConverters._

trait Monad[E, P[_]] {
  
  def pure[T]( value : T ) : P[T]
  
  def flatMap[A,T]( from : P[A], f : JFunction[A, P[T]] ) : P[T]
  
  def raiseError[T] ( error: E ) : P[T]

  def recoverWith[T]( from : P[T], f : JFunction[E, P[T]] ) : P[T]

  /**
   * Derived
   */
  
  def map[A,T]( from : P[A], f : JFunction[A, T] ) : P[T] = ???
  
  def recover[T]( from : P[T], f : JFunction[E, T] ) : P[T] = ???

  def flatten[T]( from : P[P[T]] ) : P[T] = ???

  def flatMap2[A,B,T]( fromA : P[A], fromB :P[B], f : JFunction2[A,B,P[T]] ) : P[T] = ???
  
  def map2[A,B,T]( fromA : P[A], fromB : P[B], f : JFunction2[A,B,T] ) : P[T] = ???
  
  def flatMap3[A,B,C,T]( fromA : P[A], fromB : P[B], fromC :P[C], f : JFunction3[A,B,C,P[T]] ) : P[T] = ???
  
  def map3[A,B,C,T]( fromA : P[A], fromB :P[B], fromC :P[C], f : JFunction3[A,B,C,T] ) : P[T] = ???
  
  def sequence[T]( l : JList[P[T]]  ) : P[JList[T]] =  ???
  
}
