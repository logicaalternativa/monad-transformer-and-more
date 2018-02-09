package com.logicaalternativa.monadtransformerandmore
package business

import service._

trait WithSrvBook[E, P[_]] {
     def withSrvBook ( newsrvBook : ServiceBookF[E,P] ) : WithSrvSales[E,P]
}

trait WithSrvSales[E, P[_]] {
     def withSrvSales ( newsrvSales : ServiceSalesF[E,P] ) : WithSrvChapter[E,P]
}

trait WithSrvChapter[E, P[_]] {
     def withSrvChapter ( newsrvChapter : ServiceChapterF[E,P] )  : WithSrvAuthor[E,P]
}

trait WithSrvAuthor[E, P[_]] {
     def build ( newSrvAuthor : ServiceAuthorF[E,P] ) : SrvSummaryF[E,P] 
}

trait SrvSummaryFDSL[E,P[_]] extends WithSrvBook[E,P] with WithSrvSales[E,P] with WithSrvChapter[E,P] with WithSrvAuthor[E,P] {
    
    val srvBook    : Option[ServiceBookF[E,P]]
    val srvSales   : Option[ServiceSalesF[E,P]]
    val srvChapter : Option[ServiceChapterF[E,P]]
    val srvAuthor  : Option[ServiceAuthorF[E,P]] 
  
    def withSrvBook ( newsrvBook : ServiceBookF[E,P] ) : WithSrvSales[E,P] = {
       copy( Some(newsrvBook),srvSales, srvChapter, srvAuthor )
    }
    
    def withSrvSales ( newsrvSales : ServiceSalesF[E,P] ) : WithSrvChapter[E,P] = {
        copy( srvBook, Some(newsrvSales), srvChapter, srvAuthor )
    }
    
    def withSrvChapter ( newsrvChapter : ServiceChapterF[E,P] ) : WithSrvAuthor[E,P] = {
        copy( srvBook, srvSales, Some(newsrvChapter), srvAuthor )
    }
    
    def build ( newSrvAuthor : ServiceAuthorF[E,P] ) : SrvSummaryF[E,P] 
    
    protected def copy( srvBook    : Option[ServiceBookF[E,P]]   ,
                        srvSales   : Option[ServiceSalesF[E,P]]  ,
                        srvChapter : Option[ServiceChapterF[E,P]],
                        srvAuthor  : Option[ServiceAuthorF[E,P]]   
                    ) : SrvSummaryFDSL[E,P]
}
