package com.logicaalternativa.monadtransformerandmore
package service

import bean._

trait ServiceBookF[E,P[_]] {
  
 def getBook( bookId : Int ) : P[Book]  
  
}
