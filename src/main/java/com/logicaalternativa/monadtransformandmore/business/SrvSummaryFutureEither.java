package com.logicaalternativa.monadtransformandmore.business;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformandmore.bean.Summary;

public interface SrvSummaryFutureEither<E> {
	
	Future<Either<E, Summary>> getSummary( Integer idBook );

}
