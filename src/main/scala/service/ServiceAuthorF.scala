package com.logicaalternativa.monadtransformerandmore
package service

import bean._

trait ServiceAuthorF[E,P[_]] {
  
 def getAuthor( id :String ) : P[Author]  
  
}
