package com.logicaalternativa.monadtransformerandmore.monad.impl;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import java.util.function.Function;

import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.monad.MonadContainer;
import com.logicaalternativa.monadtransformerandmore.errors.Error;

public class MonadContainerError implements MonadContainer<Error> {

	@Override
	public <T> Container<Error, T> pure(T value) {
		
		return $_notYetImpl();
			
	}

	@Override
	public <A, T> Container<Error, T> flatMap(Container<Error, A> from,
			Function<A, Container<Error, T>> f) {
		
		return $_notYetImpl();
	}

	@Override
	public <T> Container<Error, T> raiseError(Error error) {
		
		return $_notYetImpl();
		
	}

	@Override
	public <T> Container<Error, T> recoverWith(Container<Error, T> from,
			Function<Error, Container<Error, T>> f) {
		
		return $_notYetImpl();
		
	}

	

}
