package com.logicaalternativa.monadtransformerandmore.monad;
import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.function.Function3;

public interface MonadContainer<E> {
	
	/**
	 * Primitives It has to code in the implementation
	 */

	<T> Container<E, T> pure( T value );
	
	<A,T> Container<E, T> flatMap( Container<E, A> from, Function<A, Container<E, T>> f );

	
	<T> Container<E, T> raiseError( E error );
	
	<T> Container<E, T> recoverWith( Container<E, T> from, Function<E, Container<E, T>> f );

	/**
	 * Derived
	 */

	default <A,T> Container<E, T> map( Container<E, A> from, Function<A, T> f ) {

		return flatMap( from, a -> { 
			
			T t = f.apply(a);
			
			return pure( t );
			
		} );		

	}

	default <T> Container<E, T> recover( Container<E, T> from, Function<E, T> f ) {

		return recoverWith(from, 
				
				e -> {
					
					T t = f.apply(e);
					
					return pure( t );
				}
		
			) ;

	}


	default <T> Container<E, T> flatten( Container<E, Container<E, T>> from ) {

		return flatMap(from, 
				
				fut -> fut
			
			);

	}

	default <A,B,T> Container<E, T> flatMap2( Container<E, A> fromA, 
			Container<E, B> fromB, 
			BiFunction<A,B,Container<E, T>> f  ) {

		return flatten(
				map2(fromA, fromB, f)
				);

	}



	default <A,B,T> Container<E, T> map2( Container<E, A> fromA, 
			Container<E, B> fromB, 
			BiFunction<A,B,T> f  ) {

		return flatMap(fromA, 
				
				a -> map( 
							fromB,
							b ->f.apply( a, b )
							
				)
			
			)
			
			;


	}


	default <A,B,C,T> Container<E, T> flatMap3( Container<E, A> fromA, 
			Container<E, B> fromB, 
			Container<E, C> fromC, 
			Function3<A,B,C,Container<E, T>> f  ) {

		return flatten( 
				map3( fromA, fromB, fromC, f )
			);

	}

	default <A,B,C,T> Container<E, T> map3( Container<E, A> fromA, 
			Container<E, B> fromB, 
			Container<E, C> fromC, 
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
	
	default <T> Container<E, List<T>> sequence( List<Container<E, T>> l ) {

		return sequence( l.iterator() );

	}
	
	default <T> Container<E, List<T>> sequence( Iterator<Container<E, T>> i ) {

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
