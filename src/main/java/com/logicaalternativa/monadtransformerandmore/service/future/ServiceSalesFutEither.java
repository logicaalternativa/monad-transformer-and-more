package com.logicaalternativa.monadtransformerandmore.service.future;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Sales;
import com.logicaalternativa.monadtransformerandmore.service.ServiceSalesF;

public interface ServiceSalesFutEither<E> extends ServiceSalesF<E, Future> {
	
	Future<Either<E, Sales>> getSales( int bookId );

}
