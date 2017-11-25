package com.logicaalternativa.monadtransformerandmore.service.container;

import com.logicaalternativa.monadtransformerandmore.bean.Sales;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.service.ServiceSalesF;

public interface ServiceSalesContainer<E> extends ServiceSalesF<E, Container>  {
	
	Container<E, Sales> getSales( int bookId );

}
