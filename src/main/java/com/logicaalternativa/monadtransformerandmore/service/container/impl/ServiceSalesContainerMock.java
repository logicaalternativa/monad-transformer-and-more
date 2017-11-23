package com.logicaalternativa.monadtransformerandmore.service.container.impl;

import com.logicaalternativa.monadtransformerandmore.bean.Sales;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.errors.impl.MyError;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceSalesContainer;

public class ServiceSalesContainerMock implements ServiceSalesContainer<Error> {

	@Override
	public Container<Error, Sales> getSales( Integer bookId ) {
		
		if ( bookId > 999 ) {
			
			return Container.error(new MyError("Sales not found " + bookId) );
		}
		
		final int salesId = 5000*bookId;
		
		final String printed = "Book("+ bookId +") printed:  " + salesId ;
		final String numberSales = "Book("+ bookId +") sold:  " + salesId ;
		
		final Sales sales = new Sales(printed , numberSales );
		
		return Container.value( sales );
		
	}

}
