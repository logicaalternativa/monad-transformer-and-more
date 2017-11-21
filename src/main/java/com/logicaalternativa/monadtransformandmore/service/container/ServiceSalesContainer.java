package com.logicaalternativa.monadtransformandmore.service.container;

import com.logicaalternativa.monadtransformandmore.bean.Sales;
import com.logicaalternativa.monadtransformandmore.container.Container;

public interface ServiceSalesContainer<E> {
	
	Container<E, Sales> getSales( Integer bookId );

}
