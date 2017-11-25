package com.logicaalternativa.monadtransformerandmore
package service

import bean._

trait ServiceChapterF[E,P[_]] {
  
 def getChapter( idChapter :Long  ): P[Chapter]  
  
}
