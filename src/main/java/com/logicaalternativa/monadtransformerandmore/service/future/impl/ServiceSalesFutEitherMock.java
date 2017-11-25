package com.logicaalternativa.monadtransformerandmore.service.future.impl;

import scala.concurrent.Future;
import scala.util.Either;

import com.logicaalternativa.monadtransformerandmore.bean.Sales;
import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceSalesContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.impl.ServiceSalesContainerMock;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceSalesFutEither;

public class ServiceSalesFutEitherMock extends ServiceFutEitherBase implements ServiceSalesFutEither<Error> {
	
	final ServiceSalesContainer<Error> srv = new ServiceSalesContainerMock();

	@Override
	public Future<Either<Error, Sales>> getSales(int bookId) {
		
		return createFuture( () -> srv.getSales( bookId ) );
	}

	

}
