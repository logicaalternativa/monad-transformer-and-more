package com.logicaalternativa.monadtransformerandmore.service.container;

import com.logicaalternativa.monadtransformerandmore.bean.Sales;
import com.logicaalternativa.monadtransformerandmore.container.Container;

public interface ServiceSalesContainer<E>  {
	
	Container<E, Sales> getSales( Integer bookId );

}
