package com.logicaalternativa.monadtransformerandmore.monad.impl;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import java.util.function.Function;

import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.monad.MonadContainer;
import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.errors.impl.MyError;

public class MonadContainerError implements MonadContainer<Error> {

	@Override
	public <T> Container<Error, T> pure(T value) {
		
		return Container.value( value );
			
	}

	@Override
	public <A, T> Container<Error, T> flatMap(Container<Error, A> from,
			Function<A, Container<Error, T>> f) {
		
		if ( from.isOk() ) {
			
			try {
				
				return f.apply( from.getValue() );
				
			} catch(Exception e) {
				
				return raiseError( new MyError(e.getMessage()) );
				
			}
			
		} else {
			
			return raiseError( from.getError() );
			
		}
	}

	@Override
	public <T> Container<Error, T> raiseError(Error error) {
		
		return Container.error( error );
		
	}

	@Override
	public <T> Container<Error, T> recoverWith(Container<Error, T> from,
			Function<Error, Container<Error, T>> f) {
		
		if ( from.isOk() ) {
			
			return from;
			
		} else {
			
			try {			
			
				return f.apply( from.getError() );
			
			}  catch(Exception e) {
				
				return raiseError( new MyError(e.getMessage()) );
				
			}
			
		}
		
	}

	

}
