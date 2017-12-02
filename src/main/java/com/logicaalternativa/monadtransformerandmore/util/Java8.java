package com.logicaalternativa.monadtransformerandmore.util;

import java.util.function.BiConsumer;
import java.util.function.Function;

import akka.dispatch.Mapper;
import akka.dispatch.OnComplete;
import akka.dispatch.Recover;

public final class Java8 {
	
	private Java8(){}
	
	public static <T, R>  Mapper<T, R> mapperF( Function<T,R> f  ){
		
		return new Mapper<T, R>() {

			@Override
			public R apply(T parameter) {
				return f.apply(parameter);
			}
			
		};
		
	}
	
	public static <T> OnComplete<T> onCompleteF( BiConsumer<Throwable, T> bi ) {
		
		return new OnComplete<T>() {

			@Override
			public void onComplete(Throwable throwable, T value) throws Throwable {
				
				bi.accept(throwable, value );
				
			}
		};		
		
	}
	
	public static <T> Recover<T> recoverF( Function<Throwable, T> f ){		
		
		return new Recover<T>() {

			@Override
			public T recover(Throwable t ) throws Throwable {
				
				return f.apply( t );
				
			}
		};
		
	}

}
