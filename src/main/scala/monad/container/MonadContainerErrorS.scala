package com.logicaalternativa.monadtransformerandmore
package monad
package container

import function.{Function3 => JFunction3}
import com.logicaalternativa.monadtransformerandmore.container._
import errors._
import errors.impl._
import java.util.function.{Function => JFunction}
import syntax.Implicits._

import MonadContainerErrorS.ContainerError

object MonadContainerErrorS {
  
    type ContainerError[_] =  Container[Error,_]
    
    def apply() = new MonadContainerErrorS
  
}

class MonadContainerErrorS extends Monad[Error, ContainerError] {
  
  def pure[T]( value : T ) : ContainerError[T] = ???
  
  def flatMap[A,T]( from : ContainerError[A], f : JFunction[A, ContainerError[T]] ) : ContainerError[T] = ???
  
  def raiseError[T] ( error: Error ) : ContainerError[T] = ???

  def recoverWith[A,T]( from : ContainerError[A], f : JFunction[Error, ContainerError[T]] ) : ContainerError[T] = ???
  
}
