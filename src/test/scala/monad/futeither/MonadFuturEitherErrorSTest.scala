package com.logicaalternativa.monadtransformerandmore
package monad
package futeither

import errors._
import errors.impl._
import scala.concurrent._
import scala.concurrent.duration._
import scala.util._

import org.junit.Test
import org.junit.Assert._

import scala.concurrent.ExecutionContext

import MonadFutEitherS.FutEitherError

class MonadFutEitherSTest {  
  
    import scala.concurrent.ExecutionContext.Implicits.global 
  
    val M = MonadFutEitherS.apply
    
    import M._
    
    @Test
    def pureOk : Unit = {
      
        val expected = "one"
        
        val fut : FutEitherError[String] = pure( expected )
        
        val res = Await.result(fut, 500 millis )
        
        assertEquals( expected, res.right.get )
      
    }
  
    @Test
    def flatMapOk : Unit = {
      
        val expected = "one"
        
        val cont : FutEitherError[String] = pure( expected )        
        
        val fut =  flatMap[String, Int]( cont, v => pure( v.length ) )        
        
        val res = Await.result(fut, 500 millis )
        
        assertEquals( expected.length, res.right.get )        
      
    }
    
    
  
    @Test
    def flatMapKo : Unit = {
      
        val expectedError = "expected error"
        
        val cont : FutEitherError[String] = raiseError( new MyError( expectedError ) )        
        
        val fut =  flatMap[String, Int]( cont, v => pure( v.length ) )
        
        val res = Await.result( fut, 500 millis )
        
        assertEquals( expectedError, res.left.get.getDescription )        
      
    } 
  
    @Test
    def flatMapException : Unit = {
      
        val expectedError = "expected error"
        
        val cont : FutEitherError[String] = pure( "one" )       
        
        val fut =  flatMap[String, Int]( cont, v => throw new RuntimeException( expectedError ) )
        
        val res = Await.result( fut, 500 millis )
        
        assertEquals( expectedError, res.left.get.getDescription )        
      
    } 
    
    @Test
    def flatMapFailed : Unit = {
      
        val expectedError = "expected error"
        
        val cont : FutEitherError[String] = Future.failed( new Exception( expectedError ) )      
        
        val fut =  flatMap[String, Int]( cont, v => pure( v.length ) )  
               
        val res = Await.result( fut, 500 millis )
        
        assertEquals( expectedError, res.left.get.getDescription )        
      
    }
    
    @Test
    def raiseErrorOk : Unit = {
      
        val expected = "one"
        
        val fut : FutEitherError[String] = raiseError( new MyError( expected ) )
        
        val res = Await.result( fut, 500 millis )
        
        assertEquals( expected, res.left.get.getDescription )
      
    }
    
    
    
    @Test
    def recoverWithOk : Unit = {
      
        val expectedError = "expected error"
        
        val cont : FutEitherError[String] = raiseError( new MyError( expectedError ) )        
        
        val fut =  recoverWith( 
                      cont, 
                      e => pure( s"${e.getDescription} !!!" )
                    )
                    
        val res = Await.result( fut, 500 millis )
        
        assertEquals( s"$expectedError !!!", res.right.get )
      
    }
    
    @Test
    def recoverWithKo : Unit = {
      
        val expected = "expected value"
        
        val cont : FutEitherError[String] = pure( expected )        
        
        val fut =  recoverWith( 
                      cont, 
                      e => pure( s"${e.getDescription} !!!" )
                    )
                    
        val res = Await.result( fut, 500 millis )
        
        assertEquals( expected, res.right.get )
      
    }
    
    @Test
    def recoverWithException : Unit = {
      
        val expectedError = "expected error"
        
        val cont : FutEitherError[String] = raiseError( new MyError( "other error" ) )        
        
        val fut =  recoverWith( 
                      cont, 
                      e => throw new RuntimeException( expectedError ) 
                    )
                    
        val res = Await.result( fut, 500 millis )
        
        assertEquals( expectedError, res.left.get.getDescription)
      
    }
    
    @Test
    def recoverWithFailed : Unit = {
      
        val expectedError = "expected error"
        
        val cont : FutEitherError[String] = Future.failed( new Exception( expectedError ) )        
        
        val fut =  recoverWith( 
                      cont, 
                      e => pure( s"${e.getDescription} !!!" )
                    )
                    
        val res = Await.result( fut, 500 millis )
        
        assertEquals( expectedError, res.left.get.getDescription )
      
    }
  
}


