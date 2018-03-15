package com.logicaalternativa.monadtransformerandmore.monad.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.util.Either;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;

import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.errors.impl.MyError;


public class MonadFutEitherErrorTest {
    
    MonadFutEitherError m;

    @Before
    public void setUp() throws Exception {
        
        m  = new MonadFutEitherError( ExecutionContexts.global() );
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void pureOk() throws Exception{
              
        final String expected = "one";
        
        final Future<Either<Error, String>> fut = m.pure( expected );
        
        final Either<Error, String> res = Await.result(fut, Duration.apply(500, TimeUnit.MILLISECONDS) );
        
        assertEquals( expected, res.right().get() );
          
    }
      
    @Test
    public void flatMapOk() throws Exception {
          
        final String expected = "one";
            
        final Future<Either<Error, String>> cont = m.pure( expected );       
            
        final Future<Either<Error, Integer>> fut =  m.flatMap( cont, v -> m.pure( v.length() ) );       
            
        final Either<Error,Integer> res = Await.result(fut, Duration.apply(500, TimeUnit.MILLISECONDS) );
       
        assertEquals(new Integer( expected.length() ) , res.right().get() );       
         
     }
  
    @Test
    public void flatMapKo() throws Exception {
      
        final String expectedError = "expected error";;
        
        final Future<Either<Error, String>> cont = m.raiseError( new MyError( expectedError ) ) ;       
        
        final Future<Either<Error, Integer>> fut =  m.flatMap( cont, v -> m.pure( v.length() ) );
        
        final Either<Error, Integer> res = Await.result(fut, Duration.apply(500, TimeUnit.MILLISECONDS) );
        
        assertEquals( expectedError, res.left().get().getDescription() );        
      
    } 
      
    @Test
    public void flatMapException() throws Exception {
      
       final String expectedError = "expected error";
        
       final Future<Either<Error, String>> cont = m.pure( "one" );       
        
       final Future<Either<Error, Integer>> fut =  m.flatMap( cont, v -> {
                
            throw new RuntimeException( expectedError );
       });
        
       final Either<Error, Integer> res = Await.result(fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
       assertEquals( expectedError, res.left().get().getDescription() );    
      
    } 
        
    @Test
    public void flatMapFailed() throws Exception {
      
        final String expectedError = "expected error";
        
        final Future<Either<Error, String>> cont = Futures.failed( new Exception( expectedError ) );      
        
        final Future<Either<Error, Integer>> fut  =  m.flatMap( cont, v -> m.pure( v.length() ) );  
           
        final Either<Error, Integer> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( expectedError, res.left().get().getDescription() );     
      
    }
        
    @Test
    public void raiseErrorOk() throws Exception {
      
        final String expected = "one";
        
        final Future<Either<Error, String>> fut = m.<String>raiseError( new MyError( expected ) );
        
        final Either<Error, String> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( expected, res.left().get().getDescription() );
      
    }   
                
    @Test
    public void recoverWithOk() throws Exception {
          
        final String expectedError = "expected error";;
        
        final Future<Either<Error, String>> cont = m.raiseError( new MyError( expectedError ) );     
        
        final Future<Either<Error, String>>  fut =  m.recoverWith( 
                      cont, 
                      e ->  m.pure( String.format("%s !!!", e.getDescription() ) )
                    );
                    
        final Either<Error, String> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( String.format("%n !!!",expectedError), res.right().get() );
      
    }
        
    @Test
    public void recoverWithKo() throws Exception {
      
        final String expected = "expected value";
        
        final Future<Either<Error, String>> cont = m.pure( expected );       
        
        final Future<Either<Error, String>> fut =  m.recoverWith( 
              cont, 
              e ->  m.pure( String.format("%s !!!", e.getDescription() ) )
            );
                    
       final Either<Error, String> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( expected, res.right().get() );
      
    }
        
    @Test
    public void recoverWithException() throws Exception {
      
        final String expectedError = "expected error";;
    
        final Future<Either<Error, String>> cont = m.raiseError( new MyError( "other error" ) );       
        
        final Future<Either<Error, String>> fut =  m.recoverWith( 
                      cont, 
                      e -> {
                          throw new RuntimeException( expectedError );  
                      });
                    
        final Either<Error, String> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( expectedError, res.left().get().getDescription() );
      
    }
        
    @Test
    public void recoverWithFailed() throws Exception {
      
        final String expectedError = "expected error";
        
        final Future<Either<Error, String>> cont = Futures.failed( new Exception( expectedError ) );        
        
        final Future<Either<Error, String>> fut =  m.recoverWith( 
                      cont, 
                      e ->  m.pure( String.format("%s !!!", e.getDescription() ) )
                    );
                    
        final Either<Error, String> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( expectedError, res.left().get().getDescription() );
      
    }

    
    @Test
    public void mapOk() throws Exception {
      
        final String expected = "one";
        
        final Future<Either<Error,String>> cont = m.pure( expected );        
        
        final Future<Either<Error,Integer>>  fut =  m.map( cont, v -> v.length() );
        
        final Either<Error, Integer> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( new Integer( expected.length() ) , res.right().get() ) ; 
      
    }
      
    @Test
    public void mapKo() throws Exception {
      
        final String expectedError = "expected error";
        
        final Future<Either<Error,String>> cont = m.raiseError( new MyError( expectedError ) );        
        
        final Future<Either<Error,Integer>>  fut =  m.map( cont, v -> v.length() );
        
        final Either<Error, Integer> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( expectedError, res.left().get().getDescription() );        
      
    }   
    
    @Test
    public void mapException() throws Exception {
      
        final String expectedError = "expected error";
        
        final Future<Either<Error,String>> cont = m.pure( "one" );       
        
        final Future<Either<Error,Integer>> fut =  m.map( cont, v -> { 
            throw new RuntimeException( expectedError );
        } );
        
        final Either<Error, Integer> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( expectedError, res.left().get().getDescription() );        
      
    }
    

    @Test
    public void flattenOk() throws Exception {
      
        String value = "test";
      
        Future<Either<Error,String>> fut = m.flatten( m.pure( m.pure( value ) ) );
        
        final Either<Error, String> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( value, res.right().get() );
      
    }
    
    @Test
    public void flatMap2Ok() throws Exception {
    
        final Future<Either<Error,String>> one = m.pure( "one" );
        final Future<Either<Error,String>> two = m.pure( "two" );
        
        final Future<Either<Error,String>>  fut = m.flatMap2(
          one,
          two,
          ( o, t ) -> m.pure( String.format( "%n, %n", o, t )  )
        );
        
        final Either<Error, String> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( "one, two", res.right().get() );
      
    }
    
    @Test
    public void map2Ok() throws Exception {
    
        final Future<Either<Error,String>> one = m.pure( "one" );
        final Future<Either<Error,String>> two = m.pure( "two" );
        
        final Future<Either<Error,String>> fut = m.map2(
          one,
          two,
          ( o, t ) ->  String.format( "%n, %n", o, t )        
        );
        
        final Either<Error, String> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( "one, two", res.right().get() );
      
    }    
    
    @Test
    public void flatMap3Ok() throws Exception {
    
        final Future<Either<Error,String>> one = m.pure( "one" );
        final Future<Either<Error,String>> two = m.pure( "two" );
        final Future<Either<Error,String>> three = m.pure( "three" );
        
        final Future<Either<Error,String>> fut = m.flatMap3(
          one,
          two,
          three,
          ( o, t, tt ) -> m.pure( String.format( "%n, %n, %n", o, t, tt )  )
        );
        
        final Either<Error, String> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( "one, two, three" , res.right().get() );
      
    } 
    
    @Test
    public void map3Ok() throws Exception {
    
        final Future<Either<Error,String>> one = m.pure( "one" );
        final Future<Either<Error,String>> two = m.pure( "two" );
        final Future<Either<Error,String>> three = m.pure( "three" );
        
        final Future<Either<Error,String>> fut = m.map3(
          one,
          two,
          three,
          ( o, t, tt ) ->  String.format( "%n, %n, %n", o, t, tt ) 
        );
        
        final Either<Error, String> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals( "one, two, three" , res.right().get() );
      
    }
        
    @Test
    public void sequenceOk() throws Exception {
    
        final List<Future<Either<Error,String>>> list = Arrays.asList( m.pure( "one" ), m.pure( "two" ) );
        
        final Future<Either<Error, List<String>>> fut = m.sequence( list  );
        
        final Either<Error, List<String>> res = Await.result( fut, Duration.apply( 500, TimeUnit.MILLISECONDS ) );
        
        assertEquals(Arrays.asList( "one", "two" ), res.right().get() );
      
    }
 

    
    
}
