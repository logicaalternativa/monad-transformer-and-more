package com.logicaalternativa.monadtransformerandmore.business;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Summary;

public interface SrvSummaryFutureEither<E> {
	
	Future<Either<E, Summary>> getSummary( Integer idBook );

}
