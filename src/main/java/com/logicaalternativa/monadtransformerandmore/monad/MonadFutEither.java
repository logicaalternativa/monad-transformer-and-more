package com.logicaalternativa.monadtransformerandmore.monad;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.function.Function3;

public interface MonadFutEither<E> {
	
	/**
	 * Primitives It has to code in the implementation
	 */

	<T> Future<Either<E,T>> pure( T value );
	
	<A,T> Future<Either<E,T>> flatMap( Future<Either<E, A>> from, Function<A, Future<Either<E,T>>> f );

	
	<T> Future<Either<E,T>> raiseError( E error );
	
	<T> Future<Either<E,T>> recoverWith( Future<Either<E, T>> from, Function<E, Future<Either<E,T>>> f );

	/**
	 * Deriveds
	 */

	default <A,T> Future<Either<E,T>> map( Future<Either<E, A>> from, Function<A, T> f ) {

		return $_notYetImpl();

	}

	default <T> Future<Either<E,T>> recover( Future<Either<E,T>> from, Function<E, T> f ) {

		return $_notYetImpl();

	}
	
	default <T> Future<Either<E,T>> flatten( Future<Either<E,Future<Either<E,T>>>> from ) {

		return $_notYetImpl();

	}

	default <A,B,T> Future<Either<E,T>> flatMap2( Future<Either<E, A>> fromA, 
			Future<Either<E, B>> fromB, 
			BiFunction<A,B,Future<Either<E,T>>> f  ) {

		return $_notYetImpl();

	}



	default <A,B,T> Future<Either<E,T>> map2( Future<Either<E, A>> fromA, 
			Future<Either<E, B>> fromB, 
			BiFunction<A,B,T> f  ) {

		return $_notYetImpl();

	}


	default <A,B,C,T> Future<Either<E,T>> flatMap3( Future<Either<E, A>> fromA, 
			Future<Either<E, B>> fromB, 
			Future<Either<E, C>> fromC, 
			Function3<A,B,C,Future<Either<E,T>>> f  ) {

		return $_notYetImpl();

	}

	default <A,B,C,T> scala.concurrent.Future<Either<E,T>> map3( Future<Either<E, A>> fromA, 
			Future<Either<E, B>> fromB, 
			Future<Either<E, C>> fromC, 
			Function3<A,B,C,T> f  ) {

		return $_notYetImpl();

	}
	
	default <T> Future<Either<E, List<T>>> sequence( Future<Either<E, List<T>>> l ) {

		return $_notYetImpl();

	}
	
}
