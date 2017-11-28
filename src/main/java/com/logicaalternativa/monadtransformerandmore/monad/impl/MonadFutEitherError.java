package com.logicaalternativa.monadtransformerandmore.monad.impl;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import java.util.function.Function;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.util.Either;
import com.logicaalternativa.monadtransformerandmore.errors.Error;

import com.logicaalternativa.monadtransformerandmore.monad.MonadFutEither;

public class MonadFutEitherError implements MonadFutEither<Error> {
	
	final ExecutionContext ec;
	
	
	public MonadFutEitherError(ExecutionContext ec) {
		super();
		this.ec = ec;
	}

	@Override
	public <T> Future<Either<Error, T>> pure(T value) {
		
		return $_notYetImpl();
	}

	@Override
	public <A, T> Future<Either<Error, T>> flatMap(
			Future<Either<Error, A>> from,
			Function<A, Future<Either<Error, T>>> f) {
		
		return $_notYetImpl();
	}

	@Override
	public <T> Future<Either<Error, T>> raiseError(Error error) {
		
		return $_notYetImpl();
	}

	@Override
	public <T> Future<Either<Error, T>> recoverWith(
			Future<Either<Error, T>> from,
			Function<Error, Future<Either<Error, T>>> f) {
		
		return $_notYetImpl();
	}
}
