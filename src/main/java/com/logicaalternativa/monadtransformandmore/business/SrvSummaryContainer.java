package com.logicaalternativa.monadtransformandmore.business;

import com.logicaalternativa.monadtransformandmore.bean.Summary;
import com.logicaalternativa.monadtransformandmore.container.Container;

public interface SrvSummaryContainer<E> {
	
	Container<E, Summary> getSummary( Integer idBook );

}
