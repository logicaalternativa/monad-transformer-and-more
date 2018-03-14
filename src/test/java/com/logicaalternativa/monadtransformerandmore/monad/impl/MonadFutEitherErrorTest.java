package com.logicaalternativa.monadtransformerandmore.monad.impl;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.logicaalternativa.monadtransformerandmore.errors.impl.MyError;
import com.logicaalternativa.monadtransformerandmore.errors.Error;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.util.Either;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;


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

}
