package com.logicaalternativa.monadtransformerandmore
package service

import bean._

@FunctionalInterface
trait ServiceAuthorF[E,P[_]] {
  
 def getAuthor( id :String ) : P[Author]  
  
}
