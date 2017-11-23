package com.logicaalternativa.monadtransformerandmore.service.future.impl;

import java.util.concurrent.Callable;

import scala.concurrent.Future;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;

import com.logicaalternativa.monadtransformerandmore.container.Container;

public abstract class ServiceFutEitherBase {

	public ServiceFutEitherBase() {
		super();
	}

	protected <E, T> Future<Either<E, T>> createFuture(Callable <Container<E, T>> cont ) {
		return Futures.future(
				() -> fromContainerToEither( cont.call() ),
				ExecutionContexts.global() 
			);
		
		
	}

	protected <E, T> Either<E,T> fromContainerToEither( Container<E, T> cont ) {
		
		return cont.isOk()
				? new Right<E, T>( cont.getValue() )
						: new Left<E, T>( cont.getError() );
		
	}

}