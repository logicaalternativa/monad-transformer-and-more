package com.logicaalternativa.monadtransformerandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Sales;

public interface ServiceSalesFutEither<E> {
	
	Future<Either<E, Sales>> getSales( Integer bookId );

}
