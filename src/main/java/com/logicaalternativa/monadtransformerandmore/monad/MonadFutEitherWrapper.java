package com.logicaalternativa.monadtransformerandmore.monad;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.logicaalternativa.monadtransformerandmore.function.Function3;

import scala.concurrent.Future;
import scala.util.Either;

public class MonadFutEitherWrapper<E,T> {
	
	final private MonadFutEither<E> m;
	
	final private Future<Either<E, T>> fut;

	private MonadFutEitherWrapper( Future<Either<E, T>> fut, MonadFutEither<E> m) {
		this.m = m;
		this.fut = fut;
	}
	
	public static <E,T> MonadFutEitherWrapper<E,T> wrap( Future<Either<E, T>> fut, MonadFutEither<E> m) {
		
		return new MonadFutEitherWrapper<E, T>(fut, m);
				
	}
	
	public Future<Either<E, T>> value() {
		
		return fut;
		
	}
	
	public <S> MonadFutEitherWrapper<E,S> flatMap( Function<T, Future<Either<E,S>>> f ) {
		
		
		return wrap( m.flatMap(fut, f), m );
		
	}
	
	public <S> MonadFutEitherWrapper<E,S> map( Function<T, S> f ) {
		
		return wrap( m.map(fut, f), m );
		
	}
		
	public MonadFutEitherWrapper<E,T> recoverWith( Function<E, Future<Either<E,T>>> f ) {
		
		
		return wrap( m.recoverWith(fut, f), m );
		
	}

	
	public MonadFutEitherWrapper<E,T> recover( Function<E, T> f ) {
			
		return wrap( m.recover(fut, f), m );
		
	}
	
	
	public <B,S> MonadFutEitherWrapper<E,S> map2( 
			Future<Either<E, B>> fromB, 
			BiFunction<T,B,S> f  ) {
		
		
		return wrap( m.map2(fut, fromB, f), m );
		
	}
	
	
	public <B,C,S> MonadFutEitherWrapper<E,S> map3( 
			Future<Either<E, B>> fromB,  
			Future<Either<E, C>> fromC, 
			Function3<T,B,C,S> f  ) {
		
		
		return wrap( m.map3(fut, fromB,fromC, f), m );
		
	}
	
	
	

}
