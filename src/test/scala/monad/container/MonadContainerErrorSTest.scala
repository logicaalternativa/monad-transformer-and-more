package com.logicaalternativa.monadtransformerandmore
package monad
package container

import com.logicaalternativa.monadtransformerandmore.container._
import errors._
import errors.impl._

import org.junit.Test
import org.junit.Assert._

import MonadContainerErrorS.ContainerError

class MonadContainerErrorSTest {
  
    val M = MonadContainerErrorS()
    
    import M._
    
    @Test
    def pureOk : Unit = {
      
        val expected = "one"
        
        val cont : ContainerError[String] = pure( expected )
        
        assertEquals( expected, cont.getValue )
      
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
