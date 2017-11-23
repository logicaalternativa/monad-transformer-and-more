package com.logicaalternativa.monadtransformerandmore.business;

import com.logicaalternativa.monadtransformerandmore.bean.Summary;
import com.logicaalternativa.monadtransformerandmore.container.Container;

public interface SrvSummaryContainer<E> {
	
	Container<E, Summary> getSummary( Integer idBook );

}
