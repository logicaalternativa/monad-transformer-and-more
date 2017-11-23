package com.logicaalternativa.monadtransformerandmore.function;

@FunctionalInterface
public interface Function3 <A,B,C,T> {	
	
	  T apply( A a, B b, C c );

}
