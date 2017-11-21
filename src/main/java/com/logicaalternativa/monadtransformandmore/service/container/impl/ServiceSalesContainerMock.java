package com.logicaalternativa.monadtransformandmore.service.container.impl;

import com.logicaalternativa.monadtransformandmore.bean.Sales;
import com.logicaalternativa.monadtransformandmore.container.Container;
import com.logicaalternativa.monadtransformandmore.errors.Error;
import com.logicaalternativa.monadtransformandmore.service.container.ServiceSalesContainer;

public class ServiceSalesContainerMock implements ServiceSalesContainer<Error> {

	@Override
	public Container<Error, Sales> getSales( Integer bookId ) {
		
		final int salesId = 5000*bookId;
		
		final String printed = "Book("+ bookId +") printed:  " + salesId ;
		final String numberSales = "Book("+ bookId +") sold:  " + salesId ;
		
		final Sales sales = new Sales(printed , numberSales );
		
		return Container.value(sales);
		
	}

}
