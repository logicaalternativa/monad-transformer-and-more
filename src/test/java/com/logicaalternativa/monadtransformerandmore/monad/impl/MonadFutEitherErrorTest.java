package com.logicaalternativa.monadtransformerandmore.monad.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.util.Either;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;

import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.errors.impl.MyError;


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
public class MonadFutEitherErrorTest {
    
    private static final FiniteDuration DURATION = Duration.apply(2, TimeUnit.SECONDS);
	MonadFutEitherError m;

    @Before
    public void setUp() throws Exception {
        
        m  = new MonadFutEitherError( ExecutionContexts.global() );
    }

    @After
    public void tearDown() throws Exception {
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
    public void lawLeftUnit() throws Exception {
    	
    	final Future<Either<Error, String>> futB = m.pure( "b" );
		final Future<Either<Error, String>> futBerror = m.raiseError(new MyError ("errorB"));
    	
    	lawLeftUnitExec( futB );
    	lawLeftUnitExec( futBerror );
    	
    }
    
    
    private void lawLeftUnitExec( final Future<Either<Error, String>> futB ) throws Exception {
    	
    	final Future<Either<Error, String>> futA = m.pure("a");
		
    	final Future<Either<Error, String>> futBB = m.flatMap(
	    			futA, 
	    			a -> futB
    			);
    	
    	final Either<Error, String> resBB = Await.result(futBB, DURATION );
    	final Either<Error, String> resB = Await.result(futB, DURATION );
    	
    	assertEquals( resBB, resB );
    	
    }
    
    /**
     * Right unit.<pre>
     * 
     *  1) Compute m,
     *  2) bind the result to a
     *  3) return a.
     *   
     *  The result is the same as m.
     *  m * λa. unit a = m.
     *  
     * </pre>
     * @throws Exception
     */
    @Test
    public void lawRightUnit() throws Exception{
    	
    	final String expectedValue = "a";
		final Future<Either<Error, String>> futA = m.pure(expectedValue);
    	
    	final Future<Either<Error, String>> futAA = m.flatMap(
    			futA, 
    			a -> m.pure( a )
    			);
    	
    	final Either<Error, String> resAA = Await.result(futAA, DURATION );
    	final Either<Error, String> resA = Await.result(futA, DURATION );
    	
    	assertEquals( resAA, resA );
    	assertEquals( expectedValue, resAA.right().get(), resA.right().get() );
    	
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
    public void lawAsociative() throws Exception{

    	
    	final Future<Either<Error, String>> futA = m.pure( "a" );
    	final Future<Either<Error, String>> futB = m.pure( "b" );
		final Future<Either<Error, String>> futC = m.pure( "c" );
		
		
		final Future<Either<Error, String>> futAerror = m.raiseError(new MyError ("errorA"));
    	final Future<Either<Error, String>> futBerror = m.raiseError(new MyError ("errorB"));
    	final Future<Either<Error, String>> futCerror = m.raiseError(new MyError ("errorC"));
    	
    	lawAsociativeExec(futA, futB, futC);
    	lawAsociativeExec(futA, futB, futCerror);
    	
    	lawAsociativeExec(futA, futBerror, futC);
    	lawAsociativeExec(futA, futBerror, futCerror);
    	
    	lawAsociativeExec(futAerror, futB, futC);
    	lawAsociativeExec(futAerror, futB, futCerror);
    	
    	lawAsociativeExec(futAerror, futBerror, futC);
    	lawAsociativeExec(futAerror, futBerror, futCerror);
    	
    }
    
    public void lawAsociativeExec(final Future<Either<Error, String>> futA, final Future<Either<Error, String>> futB, final Future<Either<Error, String>> futC) throws Exception{

    	final Future<Either<Error, String>> futBC = m.flatMap(
    			futB,
    			b -> futC
    			);
    	
    	final Future<Either<Error, String>> futA_BC = m.flatMap(
    			futA, 
    			a -> futBC 
    			);
    	
    	final Future<Either<Error, String>> futAB = m.flatMap(
    			futA,
    			a -> futB
    			);
    	
    	final  Future<Either<Error, String>> futAB_C = m.flatMap(
    			futAB, 
    			ab -> futC
    			);
    	
    	final Either<Error, String> resA_BC = Await.result(futA_BC, DURATION );
    	final Either<Error, String> resAB_C = Await.result(futAB_C, DURATION );
    	
    	assertEquals( resA_BC, resAB_C );
    	
    }
    
    @Test
    public void pureOk() throws Exception{
              
        final String expected = "one";
        
        final Future<Either<Error, String>> fut = m.pure( expected );
        
        final Either<Error, String> res = Await.result(fut, DURATION );
        
        assertEquals( expected, res.right().get() );
          
    }
      
    @Test
    public void flatMapOk() throws Exception {
          
        final String expected = "one";
            
        final Future<Either<Error, String>> cont = m.pure( expected );       
            
        final Future<Either<Error, Integer>> fut =  m.flatMap( cont, v -> m.pure( v.length() ) );       
            
        final Either<Error,Integer> res = Await.result(fut, DURATION );
       
        assertEquals(new Integer( expected.length() ) , res.right().get() );       
         
     }
  
    @Test
    public void flatMapKo() throws Exception {
      
        final String expectedError = "expected error";;
        
        final Future<Either<Error, String>> cont = m.raiseError( new MyError( expectedError ) ) ;       
        
        final Future<Either<Error, Integer>> fut =  m.flatMap( cont, v -> m.pure( v.length() ) );
        
        final Either<Error, Integer> res = Await.result(fut, DURATION );
        
        assertEquals( expectedError, res.left().get().getDescription() );        
      
    } 
      
    @Test
    public void flatMapException() throws Exception {
      
       final String expectedError = "expected error";
        
       final Future<Either<Error, String>> cont = m.pure( "one" );       
        
       final Future<Either<Error, Integer>> fut =  m.flatMap( cont, v -> {
                
            throw new RuntimeException( expectedError );
       });
        
       final Either<Error, Integer> res = Await.result(fut, DURATION );
        
       assertEquals( expectedError, res.left().get().getDescription() );    
      
    } 
        
    @Test
    public void flatMapFailed() throws Exception {
      
        final String expectedError = "expected error";
        
        final Future<Either<Error, String>> cont = Futures.failed( new Exception( expectedError ) );      
        
        final Future<Either<Error, Integer>> fut  =  m.flatMap( cont, v -> m.pure( v.length() ) );  
           
        final Either<Error, Integer> res = Await.result( fut, DURATION );
        
        assertEquals( expectedError, res.left().get().getDescription() );     
      
    }
        
    @Test
    public void raiseErrorOk() throws Exception {
      
        final String expected = "one";
        
        final Future<Either<Error, String>> fut = m.<String>raiseError( new MyError( expected ) );
        
        final Either<Error, String> res = Await.result( fut, DURATION );
        
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
                    
        final Either<Error, String> res = Await.result( fut, DURATION );
        
        assertEquals( String.format("%s !!!",expectedError), res.right().get() );
      
    }
        
    @Test
    public void recoverWithKo() throws Exception {
      
        final String expected = "expected value";
        
        final Future<Either<Error, String>> cont = m.pure( expected );       
        
        final Future<Either<Error, String>> fut =  m.recoverWith( 
              cont, 
              e ->  m.pure( String.format("%s !!!", e.getDescription() ) )
            );
                    
       final Either<Error, String> res = Await.result( fut, DURATION );
        
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
                    
        final Either<Error, String> res = Await.result( fut, DURATION );
        
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
                    
        final Either<Error, String> res = Await.result( fut, DURATION );
        
        assertEquals( String.format("%s !!!", expectedError), res.right().get() );
      
    }

    
    @Test
    public void mapOk() throws Exception {
      
        final String expected = "one";
        
        final Future<Either<Error,String>> cont = m.pure( expected );        
        
        final Future<Either<Error,Integer>>  fut =  m.map( cont, v -> v.length() );
        
        final Either<Error, Integer> res = Await.result( fut, DURATION );
        
        assertEquals( new Integer( expected.length() ) , res.right().get() ) ; 
      
    }
      
    @Test
    public void mapKo() throws Exception {
      
        final String expectedError = "expected error";
        
        final Future<Either<Error,String>> cont = m.raiseError( new MyError( expectedError ) );        
        
        final Future<Either<Error,Integer>>  fut =  m.map( cont, v -> v.length() );
        
        final Either<Error, Integer> res = Await.result( fut, DURATION );
        
        assertEquals( expectedError, res.left().get().getDescription() );        
      
    }   
    
    @Test
    public void mapException() throws Exception {
      
        final String expectedError = "expected error";
        
        final Future<Either<Error,String>> cont = m.pure( "one" );       
        
        final Future<Either<Error,Integer>> fut =  m.map( cont, v -> { 
            throw new RuntimeException( expectedError );
        } );
        
        final Either<Error, Integer> res = Await.result( fut, DURATION );
        
        assertEquals( expectedError, res.left().get().getDescription() );        
      
    }
    

    @Test
    public void flattenOk() throws Exception {
      
        String value = "test";
      
        Future<Either<Error,String>> fut = m.flatten( m.pure( m.pure( value ) ) );
        
        final Either<Error, String> res = Await.result( fut, DURATION );
        
        assertEquals( value, res.right().get() );
      
    }
    
    @Test
    public void flatMap2Ok() throws Exception {
    
        final Future<Either<Error,String>> one = m.pure( "one" );
        final Future<Either<Error,String>> two = m.pure( "two" );
        
        final Future<Either<Error,String>>  fut = m.flatMap2(
          one,
          two,
          ( o, t ) -> m.pure( String.format( "%s, %s", o, t )  )
        );
        
        final Either<Error, String> res = Await.result( fut, DURATION );
        
        assertEquals( "one, two", res.right().get() );
      
    }
    
    @Test
    public void map2Ok() throws Exception {
    
        final Future<Either<Error,String>> one = m.pure( "one" );
        final Future<Either<Error,String>> two = m.pure( "two" );
        
        final Future<Either<Error,String>> fut = m.map2(
          one,
          two,
          ( o, t ) ->  String.format( "%s, %s", o, t )        
        );
        
        final Either<Error, String> res = Await.result( fut, DURATION );
        
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
          ( o, t, tt ) -> m.pure( String.format( "%s, %s, %s", o, t, tt )  )
        );
        
        final Either<Error, String> res = Await.result( fut, DURATION );
        
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
          ( o, t, tt ) ->  String.format( "%s, %s, %s", o, t, tt ) 
        );
        
        final Either<Error, String> res = Await.result( fut, DURATION );
        
        assertEquals( "one, two, three" , res.right().get() );
      
    }
        
    @Test
    public void sequenceOk() throws Exception {
    
        final List<Future<Either<Error,String>>> list = Arrays.asList( m.pure( "one" ), m.pure( "two" ) );
        
        final Future<Either<Error, List<String>>> fut = m.sequence( list  );
        
        final Either<Error, List<String>> res = Await.result( fut, DURATION );
        
        assertEquals(Arrays.asList( "one", "two" ), res.right().get() );
      
    }
 

    
    
}
