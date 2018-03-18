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

object MonadFutEitherSTest {
    
    val Duration = 500 millis
    
}
 
 /**
 * <pre>
 * Monad laws from 
 * 
 * [Monads for functional programming](http://homepages.inf.ed.ac.uk/wadler/papers/marktoberdorf/baastad.pdf)
 * 
 * A binary operation with left and right unit that is associative is called a 
 * monoid.
 * 
 * A monad differs from a monoid in that the right operand involves a binding
 * operation.
 * 
 * </pre>
 * 
 * @author miguel.esteban@logicaalternativa.com
 *
 */
 class MonadFutEitherSTest {  
    
    import MonadFutEitherSTest.Duration
    
    import Await.result
  
    import scala.concurrent.ExecutionContext.Implicits.global 
  
    val M = MonadFutEitherS.apply
    
    import M._
    
    /**
     * Left unit <pre>
     * 
     *  1) Compute the value a
     *  2) bind b to the result
     *  3) compute n
     *  
     *  The result is the same as n with value a substituted for variable b
     *  
     *  unit a * λb. n = n[a/b]
     *  
     *  </pre>
     * @throws Exception
     */
    @Test
    def lawLeftUnit = {
        
        val futB = pure( "b" )
        val futBerror = raiseError( new MyError( "errorB" ) )
        
        lawLeftUnitExec( futB )
        lawLeftUnitExec( futBerror )
        
    }
    
    private def lawLeftUnitExec( futB : FutEitherError[String] ) = {
        
        val futA = pure("a")
        
        val futBB = flatMap[String, String](
                futA, 
                a => futB
                )
        
        val resBB = result(futBB, Duration )
        val resB = result(futB, Duration )
        
        assertEquals( resBB.right.get, resB.right.get )
        
    }
    
     /**
     * Right unit.<pre>
     * 
     *  1) Compute m,
     *  2) bind the result to a
     *  3) return a.
     *   
     *  The result is the same as 
     *  m * λa. unit a = 
     *  
     * </pre>
     * @throws Exception
     */
    @Test
    def lawRightUnit = {

        val expectedValue = "a"
        val futA = pure(expectedValue)

        val futAA = flatMap[String, String](
                        futA, 
                        a => pure( a )
                    )

        val resAA = result(futAA, Duration )
        val resA = result(futA, Duration )

        assertEquals( resAA, resA )
        assertEquals( expectedValue, resAA.right.get, resA.right.get )
        
    }
    
    
    /**
     * Associative. <pre>
     * 
     *  1) Compute m
     *  2) bind the result to a
     *  3) compute n, bind the result to b
     *  4) compute o.
     *  
     *   The order of parentheses in such a computation is irrelevant.
     *    m * (λa. n * λb. o) = (m * λa. n) * λb. o
     *        
     * </pre>
     * @throws Exception
     */    
    @Test
    def lawAsociative = {

        val futA = pure( "a" )
        val futB = pure( "b" )
        val futC = pure( "c" )
        
        val futAerror = raiseError(new MyError ("errorA"))
        val futBerror = raiseError(new MyError ("errorB"))
        val futCerror = raiseError(new MyError ("errorC"))
        
        lawAsociativeExec(futA, futB, futC)
        lawAsociativeExec(futA, futB, futCerror)
        
        lawAsociativeExec(futA, futBerror, futC)
        lawAsociativeExec(futA, futBerror, futCerror)
        
        lawAsociativeExec(futAerror, futB, futC)
        lawAsociativeExec(futAerror, futB, futCerror)
        
        lawAsociativeExec(futAerror, futBerror, futC)
        lawAsociativeExec(futAerror, futBerror, futCerror)
        
    }
    
    private def lawAsociativeExec( futA : FutEitherError[String], futB : FutEitherError[String], futC : FutEitherError[String] ) = {

        val futBC = flatMap[String, String](
                futB,
                b => futC
                )

        val futA_BC = flatMap[String, String](
                futA, 
                a => futBC 
                )

        val futAB = flatMap[String, String](
                futA,
                a => futB
                )

        val futAB_C = flatMap[String, String](
                futAB, 
                ab => futC
                )

        val resA_BC = result(futA_BC, Duration )
        val resAB_C = result(futAB_C, Duration )

        assertEquals( resA_BC, resAB_C )

    }
    
    @Test
    def pureOk : Unit = {
      
        val expected = "one"
        
        val fut : FutEitherError[String] = pure( expected )
        
        val res = result(fut, Duration )
        
        assertEquals( expected, res.right.get )
      
    }
  
    @Test
    def flatMapOk : Unit = {
      
        val expected = "one"
        
        val cont : FutEitherError[String] = pure( expected )        
        
        val fut =  flatMap[String, Int]( cont, v => pure( v.length ) )        
        
        val res = result(fut, Duration )
        
        assertEquals( expected.length, res.right.get )        
      
    }
    
    
  
    @Test
    def flatMapKo : Unit = {
      
        val expectedError = "expected error"
        
        val cont : FutEitherError[String] = raiseError( new MyError( expectedError ) )        
        
        val fut =  flatMap[String, Int]( cont, v => pure( v.length ) )
        
        val res = result( fut, Duration )
        
        assertEquals( expectedError, res.left.get.getDescription )        
      
    } 
  
    @Test
    def flatMapException : Unit = {
      
        val expectedError = "expected error"
        
        val cont : FutEitherError[String] = pure( "one" )       
        
        val fut =  flatMap[String, Int]( cont, v => throw new RuntimeException( expectedError ) )
        
        val res = result( fut, Duration )
        
        assertEquals( expectedError, res.left.get.getDescription )        
      
    } 
    
    @Test
    def flatMapFailed : Unit = {
      
        val expectedError = "expected error"
        
        val cont : FutEitherError[String] = Future.failed( new Exception( expectedError ) )      
        
        val fut =  flatMap[String, Int]( cont, v => pure( v.length ) )  
               
        val res = result( fut, Duration )
        
        assertEquals( expectedError, res.left.get.getDescription )        
      
    }
    
    @Test
    def raiseErrorOk : Unit = {
      
        val expected = "one"
        
        val fut : FutEitherError[String] = raiseError( new MyError( expected ) )
        
        val res = result( fut, Duration )
        
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
                    
        val res = result( fut, Duration )
        
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
                    
        val res = result( fut, Duration )
        
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
                    
        val res = result( fut, Duration )
        
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
                    
        val res = result( fut, Duration )
        
        assertEquals( expectedError, res.left.get.getDescription )
      
    }
  
}


