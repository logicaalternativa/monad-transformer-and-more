package com.logicaalternativa.monadtransformerandmore
package service

import bean._

@FunctionalInterface
trait ServiceSalesF[E,P[_]] {
  
 def getSales( bookId : Int ) : P[Sales]  
  
}
