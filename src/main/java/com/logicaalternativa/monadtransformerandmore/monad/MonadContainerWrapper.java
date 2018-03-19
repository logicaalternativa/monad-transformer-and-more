package com.logicaalternativa.monadtransformerandmore.monad;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.function.Function3;

public class MonadContainerWrapper<E,T> {
	
	final private MonadContainer<E> m;
	
	final private Container<E, T> cont;

	private MonadContainerWrapper( Container<E, T> cont, MonadContainer<E> m) {
		this.m = m;
		this.cont = cont;
	}
	
	public static <E,T> MonadContainerWrapper<E,T> wrap( Container<E, T> cont, MonadContainer<E> m) {
		
		return new MonadContainerWrapper<E, T>(cont, m);
				
	}
	
	public Container<E, T> value() {
		
		return cont;
		
	}
	
	public <S> MonadContainerWrapper<E,S> flatMap( Function<T, Container<E,S>> f ) {
		
		
		return wrap( m.flatMap(cont, f), m );
		
	}
	
	public <S> MonadContainerWrapper<E,S> map( Function<T, S> f ) {
		
		return wrap( m.map(cont, f), m );
		
	}
		
	public MonadContainerWrapper<E,T> recoverWith( Function<E, Container<E,T>> f ) {
		
		
		return wrap( m.recoverWith(cont, f), m );
		
	}

	
	public MonadContainerWrapper<E,T> recover( Function<E, T> f ) {
			
		return wrap( m.recover(cont, f), m );
		
	}
	
	
	public <B,S> MonadContainerWrapper<E,S> map2( 
			Container<E, B> fromB, 
			BiFunction<T,B,S> f  ) {
		
		
		return wrap( m.map2(cont, fromB, f), m );
		
	}
	
	
	public <B,C,S> MonadContainerWrapper<E,S> map3( 
			Container<E, B> fromB,  
			Container<E, C> fromC, 
			Function3<T,B,C,S> f  ) {
		
		
		return wrap( m.map3(cont, fromB,fromC, f), m );
		
	}
	
	
	

}
