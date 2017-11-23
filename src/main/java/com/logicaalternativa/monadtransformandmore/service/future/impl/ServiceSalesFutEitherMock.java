package com.logicaalternativa.monadtransformandmore.service.future.impl;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformandmore.bean.Sales;
import com.logicaalternativa.monadtransformandmore.errors.Error;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceSalesContainer;
import com.logicaalternativa.monadtransformandmore.service.container.impl.ServiceSalesContainerMock;
import com.logicaalternativa.monadtransformandmore.service.future.ServiceSalesFutEither;

public class ServiceSalesFutEitherMock extends ServiceFutEitherBase implements ServiceSalesFutEither<Error> {
	
	final ServiceSalesContainer<Error> srv = new ServiceSalesContainerMock();

	@Override
	public Future<Either<Error, Sales>> getSales(Integer bookId) {
		
		return createFuture( () -> srv.getSales( bookId ) );
	}

	

}
