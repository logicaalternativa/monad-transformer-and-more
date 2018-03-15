package com.logicaalternativa.monadtransformerandmore.monad;

import java.util.Iterator;
import java.util.LinkedList;
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

		return flatMap( from, a -> { 
			
			T t = f.apply(a);
			
			return pure( t );
			
		} );

	}

	default <T> Future<Either<E,T>> recover( Future<Either<E,T>> from, Function<E, T> f ) {

		return recoverWith(from, 
				
				e -> {
					
					T t = f.apply(e);
					
					return pure( t );
				}
		
			) ;

	}
	
	default <T> Future<Either<E,T>> flatten( Future<Either<E,Future<Either<E,T>>>> from ) {

		return flatMap(from, 
					
					fut -> fut
				
				);

	}

	default <A,B,T> Future<Either<E,T>> flatMap2( Future<Either<E, A>> fromA, 
			Future<Either<E, B>> fromB, 
			BiFunction<A,B,Future<Either<E,T>>> f  ) {
		
		return flatten(
				map2(fromA, fromB, f)
				);
	}



	default <A,B,T> Future<Either<E,T>> map2( Future<Either<E, A>> fromA, 
			Future<Either<E, B>> fromB, 
			BiFunction<A,B,T> f  ) {

		return flatMap(fromA, 
				
					a -> map( 
								fromB,
								b ->f.apply( a, b )
								
					)
				
				)
				
				;

	}


	default <A,B,C,T> Future<Either<E,T>> flatMap3( Future<Either<E, A>> fromA, 
			Future<Either<E, B>> fromB, 
			Future<Either<E, C>> fromC, 
			Function3<A,B,C,Future<Either<E,T>>> f  ) {

		return flatten( 
					map3( fromA, fromB, fromC, f )
				);

	}

	default <A,B,C,T> scala.concurrent.Future<Either<E,T>> map3( Future<Either<E, A>> fromA, 
			Future<Either<E, B>> fromB, 
			Future<Either<E, C>> fromC, 
			Function3<A,B,C,T> f  ) {
		
		
		return flatMap(
				fromA, 
				 a -> map2(
							fromB,
						 	fromC,
								( b, c ) -> f.apply(a, b, c)
							)
			);

	}
	
	default <T> Future<Either<E, List<T>>> sequence( List<Future<Either<E, T>>> l ) {

		return sequence( l.iterator() );

	}
	
	default <T> Future<Either<E, List<T>>> sequence( Iterator <Future<Either<E, T>>> i ) {
		
		if (! i.hasNext() ) {
			
			return pure( new LinkedList<>() );
			
		}
		
		return map2(	
				i.next(), 
				sequence( i ), 
				(actual, list ) -> {					
										final LinkedList<T> newList = new LinkedList<>( list );
										newList.addFirst( actual );
										return newList;
									}
				);

	}
	
}
