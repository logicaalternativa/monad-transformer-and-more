package com.logicaalternativa.monadtransformandmore.monad;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.logicaalternativa.monadtransformandmore.container.Container;
import com.logicaalternativa.monadtransformandmore.function.Function3;

import static com.logicaalternativa.monadtransformandmore.util.Util.$_notYetImpl;

public interface MonadContainer<E> {
	
	/**
	 * Primitives It has to code in the implementation
	 */

	<T> Container<E, T> pure( T value );
	
	<A,T> Container<E, T> flatMap( Container<E, A> from, Function<A, Container<E, T>> f );

	
	<T> Container<E, T> raiseError( E error );
	
	<A,T> Container<E, T> recoverWith( Container<E, A> from, Function<E, Container<E, T>> f );

	/**
	 * Derived
	 */

	default <A,T> Container<E, T> map( Container<E, A> from, Function<A, T> f ) {

		return $_notYetImpl();

	}

	default <T> Container<E, T> recover( Container<E, T> from, Function<E, T> f ) {

		return $_notYetImpl();

	}

	default <A,B,T> Container<E, T> flapMap2( Container<E, A> fromA, 
			Container<E, B> fromB, 
			BiFunction<A,B,Container<E, T>> f  ) {

		return $_notYetImpl();

	}



	default <A,B,T> Container<E, T> map2( Container<E, A> fromA, 
			Container<E, B> fromB, 
			BiFunction<A,B,T> f  ) {

		return $_notYetImpl();

	}


	default <A,B,C,T> Container<E, T> flapMap3( Container<E, A> fromA, 
			Container<E, B> fromB, 
			Container<E, C> fromC, 
			Function3<A,B,C,Container<E, T>> f  ) {

		return $_notYetImpl();

	}

	default <A,B,C,T> Container<E, T> map3( Container<E, A> fromA, 
			Container<E, B> fromB, 
			Container<E, C> fromC, 
			Function3<A,B,C,T> f  ) {

		return $_notYetImpl();

	}
	
	default <T> Container<E, List<T>> secuence( Container<E, List<T>> l ) {

		return $_notYetImpl();

	}
	
}
