package com.logicaalternativa.monadtransformandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformandmore.bean.Sales;

public interface ServiceSalesFutEither<E> {
	
	Future<Either<E, Sales>> getSales( Integer bookId );

}
