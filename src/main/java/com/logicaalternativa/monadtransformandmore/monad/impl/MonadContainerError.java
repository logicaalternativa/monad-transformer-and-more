package com.logicaalternativa.monadtransformandmore.monad.impl;

import static com.logicaalternativa.monadtransformandmore.util.Util.$_notYetImpl;

import java.util.function.Function;

import com.logicaalternativa.monadtransformandmore.container.Container;
import com.logicaalternativa.monadtransformandmore.errors.Error;
import com.logicaalternativa.monadtransformandmore.monad.MonadContainer;

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
	public <A, T> Container<Error, T> recoverWith(Container<Error, A> from,
			Function<Error, Container<Error, T>> f) {
		
		return $_notYetImpl();
		
	}

	

}
