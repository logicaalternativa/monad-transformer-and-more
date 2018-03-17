package com.logicaalternativa.monadtransformerandmore
package monad
package container

import com.logicaalternativa.monadtransformerandmore.container._
import errors._
import errors.impl._

import org.junit.Test
import org.junit.Assert._

import MonadContainerErrorS.ContainerError

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
 class MonadContainerErrorSTest {
  
    val M = MonadContainerErrorS()
    
    import M._
    
    @Test
    def pureOk : Unit = {
      
        val expected = "one"
        
        val cont : ContainerError[String] = pure( expected )
        
        assertEquals( expected, cont.getValue )
    }
    
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
        
        val expectedValue = "b"

        val contA = pure("a")
        val contB = pure( expectedValue )
        
        val contBB = flatMap[String, String](
                contA, 
                a => contB
                )
        
        assertEquals( contBB, contB )
        assertEquals( expectedValue, contBB.getValue, contB.getValue )
        
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
        val contA = pure(expectedValue)

        val contAA = flatMap[String, String](
                        contA, 
                        a => pure( a )
                    )

        assertEquals( contAA, contA )
        assertEquals( expectedValue, contAA.getValue, contA.getValue )
        
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

        val expectedValue = "c"

        val contA = pure( "a" )
        val contB = pure( "b" )
        val contC = pure( expectedValue )

        val contBC = flatMap[String, String](
                contB,
                b => contC
                )

        val contA_BC = flatMap[String, String](
                contA, 
                a => contBC 
                )

        val contAB = flatMap[String, String](
                contA,
                a => contB
                )

        val contAB_C = flatMap[String, String](
                contAB, 
                ab => contC
                )
        
        assertEquals( contA_BC, contAB_C )
        assertEquals( expectedValue, contA_BC.getValue, contAB_C.getValue )
        
    }
  
    @Test
    def flatMapOk : Unit = {
      
        val expected = "one"
        
        val cont : ContainerError[String] = Container.value( expected )        
        
        val res =  flatMap[String, Int]( cont, v => Container.value( v.length ) )
        
        assertEquals( expected.length, res.getValue )        
      
    }
  
    @Test
    def flatMapKo : Unit = {
      
        val expectedError = "expected error"
        
        val cont : ContainerError[String] = Container.error( new MyError( expectedError ) )        
        
        val res =  flatMap[String, Int]( cont, v => Container.value( v.length ) )
        
        assertEquals( expectedError, res.getError.getDescription )        
      
    }   
    
  
    @Test
    def flatMapException : Unit = {
      
        val expectedError = "expected error"
        
        val cont : ContainerError[String] = Container.value( "one" )       
        
        val res =  flatMap[String, Int]( cont, v => throw new RuntimeException( expectedError ) )
        
        assertEquals( expectedError, res.getError.getDescription )        
      
    }
    
    @Test
    def raiseErrorOk : Unit = {
      
        val expected = "one"
        
        val cont : ContainerError[String] = raiseError( new MyError( expected ) )
        
        assertEquals( expected, cont.getError.getDescription )
      
    }
    
    @Test
    def recoverWithOk : Unit = {
      
        val expectedError = "expected error"
        
        val cont : ContainerError[String] = Container.error( new MyError( expectedError ) )        
        
        val res =  recoverWith( 
                      cont, 
                      e => Container.value( s"${e.getDescription} !!!" )
                    )
        
        assertEquals( s"$expectedError !!!", res.getValue )
      
    }
    
    @Test
    def recoverWithKo : Unit = {
      
        val expected = "expected value"
        
        val cont : ContainerError[String] = Container.value( expected )        
        
        val res =  recoverWith( 
                      cont, 
                      e => Container.value( s"${e.getDescription} !!!" )
                    )
        
        assertEquals( expected, res.getValue )
      
    }
    
    @Test
    def recoverWithException : Unit = {
      
        val expectedError = "expected error"
        
        val cont : ContainerError[String] = Container.error( new MyError( "other error" ) )        
        
        val res =  recoverWith( 
                      cont, 
                      e => throw new RuntimeException( expectedError ) 
                    )
        
        assertEquals( expectedError, res.getError.getDescription )
      
    }
    
    @Test
    def mapOk : Unit = {
      
        val expected = "one"
        
        val cont : ContainerError[String] = Container.value( expected )        
        
        val res =  map[String, Int]( cont, v => v.length  )
        
        assertEquals( expected.length, res.getValue )  
      
    }
    
    
  
    @Test
    def mapKo : Unit = {
      
        val expectedError = "expected error"
        
        val cont : ContainerError[String] = Container.error( new MyError( expectedError ) )        
        
        val res =  map[String, Int]( cont, v => v.length  )
        
        assertEquals( expectedError, res.getError.getDescription )        
      
    }   
    
  
    @Test
    def mapException : Unit = {
      
        val expectedError = "expected error"
        
        val cont : ContainerError[String] = Container.value( "one" )       
        
        val res =  map[String, Int]( cont, v => throw new RuntimeException( expectedError ) )
        
        assertEquals( expectedError, res.getError.getDescription )        
      
    }
    
    
    
    @Test
    def recoverOk : Unit = {
      
        val expectedError = "expected error"
        
        val cont : ContainerError[String] = Container.error( new MyError( expectedError ) )        
        
        val res =  recover( 
                      cont, 
                      e => s"${e.getDescription} !!!"
                    )
        
        assertEquals( s"$expectedError !!!", res.getValue )
      
    }
    
    @Test
    def recoverKo : Unit = {
      
        val expected = "expected value"
        
        val cont : ContainerError[String] = Container.value( expected )        
        
        val res =  recover( 
                      cont, 
                      e => s"${e.getDescription} !!!" 
                    )
        
        assertEquals( expected, res.getValue )
      
    }
    
    @Test
    def recoverException : Unit = {
      
        val expectedError = "expected error"
        
        val cont : ContainerError[String] = Container.error( new MyError( "other error" ) )        
        
        val res =  recover( 
                      cont, 
                      e => throw new RuntimeException( expectedError ) 
                    )
        
        assertEquals( expectedError, res.getError.getDescription )
      
    }
    
    @Test
    def flattenOk : Unit = {
      
        val value = "test"
      
        val res = flatten( pure( pure( value ) ) )
        
        assertEquals( value, res.getValue )
      
    }
    
    @Test
    def flatMap2Ok : Unit = {
    
        val one = pure( "one" )
        val two = pure( "two" )
        
        val res = flatMap2[String, String, String](
          one,
          two,
          ( o, t ) => pure( s"$o, $t" )
        
        )
        
        assertEquals( "one, two" , res.getValue )
      
    }
    
    @Test
    def map2Ok : Unit = {
    
        val one = pure( "one" )
        val two = pure( "two" )
        
        val res = map2[String, String, String](
          one,
          two,
          ( o, t ) =>  s"$o, $t"
        
        )
        
        assertEquals( "one, two" , res.getValue )
      
    }    
    
    @Test
    def flatMap3Ok : Unit = {
    
        val one = pure( "one" )
        val two = pure( "two" )
        val three = pure( "three" )
        
        val res = flatMap3[String, String, String, String](
          one,
          two,
          three,
          ( o, t, tt ) => pure( s"$o, $t, $tt" )
        )
        
        assertEquals( "one, two, three" , res.getValue )
      
    } 
    
    @Test
    def map3Ok : Unit = {
    
        val one = pure( "one" )
        val two = pure( "two" )
        val three = pure( "three" )
        
        val res = map3[String, String, String, String](
          one,
          two,
          three,
          ( o, t, tt ) =>  s"$o, $t, $tt"
        )
        
        assertEquals( "one, two, three" , res.getValue )
      
    }
    
    
    @Test
    def sequenceOk : Unit = {
    
        val list = List( pure( "one" ), pure( "two" ) )
        
        val res = sequence( list  )
        
        assertEquals( List( "one", "two" ) , res.getValue )
      
    }
  
}
