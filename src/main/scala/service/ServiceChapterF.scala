package com.logicaalternativa.monadtransformerandmore
package service

import bean._

@FunctionalInterface
trait ServiceChapterF[E,P[_]] {
  
 def getChapter( idChapter :Long  ): P[Chapter]  
  
}
