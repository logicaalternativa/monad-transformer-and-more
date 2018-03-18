package com.logicaalternativa.monadtransformerandmore.monad.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.logicaalternativa.monadtransformerandmore.container.Container;
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

public class MonadContainerErrorTest {
    
    MonadContainerError m ;

    @Before
    public void setUp() throws Exception {
        m = new MonadContainerError();
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
    public void lawLeftUnit() throws Exception{
    	
    	final Container<Error, String> contB = m.pure( "b" );
		final Container<Error, String> contBerror = m.raiseError(new MyError ("errorB"));
		
    	lawLeftUnitExec( contB );
    	lawLeftUnitExec( contBerror );
		
		
    }
    
    private void lawLeftUnitExec( final Container<Error, String> contB ) throws Exception{
    	
    	final Container<Error, String> contA = m.pure("a");
    	
    	final Container<Error, String> contBB = m.flatMap(
    			contA, 
    			a -> contB
    			);
    	
    	
    	assertEquals( contBB, contB );
    	
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
		final Container<Error,String> contA = m.pure(expectedValue);
    	
    	final Container<Error,String> contAA = m.flatMap(
    			contA, 
    			a -> m.pure( a )
    			);
    	
    	assertEquals( contAA, contA );
    	assertEquals( expectedValue, contAA.getValue(), contA.getValue() );
    	
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
    public void lawAsociative() {
    	
    	final Container<Error, String> contA = m.pure( "a" );
    	final Container<Error, String> contB = m.pure( "b" );
    	final Container<Error, String> contC = m.pure( "c" );
    	
    	final Container<Error, String> contAerror = m.raiseError(new MyError ("errorA"));
    	final Container<Error, String> contBerror = m.raiseError(new MyError ("errorB"));
    	final Container<Error, String> contCerror = m.raiseError(new MyError ("errorC"));
    	
    	lawAsociativeExec(contA, contB, contC);
    	lawAsociativeExec(contA, contB, contCerror);
    	
    	lawAsociativeExec(contA, contBerror, contC);
    	lawAsociativeExec(contA, contBerror, contCerror);
    	
    	lawAsociativeExec(contAerror, contB, contC);
    	lawAsociativeExec(contAerror, contB, contCerror);
    	
    	lawAsociativeExec(contAerror, contBerror, contC);
    	lawAsociativeExec(contAerror, contBerror, contCerror);
    	
    	
    }
    
    private void lawAsociativeExec(final Container<Error, String> contA, final Container<Error, String> contB, final Container<Error, String> contC) {

    	
    	final Container<Error, String> contBC = m.flatMap(
    			contB,
    			b -> contC
    			);
    	
    	final Container<Error, String> contA_BC = m.flatMap(
    			contA, 
    			a -> contBC 
    			);
    	
    	final Container<Error, String> contAB = m.flatMap(
    			contA,
    			a -> contB
    			);
    	
    	final  Container<Error, String> contAB_C = m.flatMap(
    			contAB, 
    			ab -> contC
    			);
    	
    	assertEquals( contA_BC, contAB_C );
    	
    	
    }
    

    @Test
    public void pureOk() {
      
        final String expected = "one";
        
        final Container<Error,String> cont = m.pure( expected );
        
        assertEquals( expected, cont.getValue() );
      
    }
  
    @Test
    public void flatMapOk() {
      
        final String expected = "one";
        
        final Container<Error,String> cont = Container.value( expected );        
        
        final Container<Error,Integer> res =  m.flatMap( cont, v -> Container.value( v.length() ) );
        
        assertEquals( new Integer( expected.length() ) , res.getValue() );
      
    }
  
    @Test
    public void flatMapKo() {
      
        final String expectedError = "expected error";
        
        final Container<Error,String> cont = Container.error( new MyError( expectedError ) );        
        
        final Container<Error,Integer>  res =  m.flatMap( cont, v -> Container.value( v.length() ) );
        
        assertEquals( expectedError, res.getError().getDescription() );        
      
    }   
  
    @Test
    public void flatMapException() {
      
        final String expectedError = "expected error";
        
        final Container<Error,String> cont = Container.value( "one" );      
        
        final Container<Error,String> res =  m.flatMap( cont, v -> {
            
            throw new RuntimeException( expectedError );
        });
        
        assertEquals( expectedError, res.getError().getDescription() );        
      
    }
    
    @Test
    public void raiseErrorOk() {
      
        final String expected = "one";
        
        final Container<Error,String> cont = m.raiseError( new MyError( expected ) );
        
        assertEquals( expected, cont.getError().getDescription() );
      
    }
    
    @Test
    public void recoverWithOk() {
      
        final String expectedError = "expected error";
        
        final Container<Error,String> cont = Container.error( new MyError( expectedError ) );        
        
        final Container<Error,String> res =  m.recoverWith( 
                      cont, 
                      e -> Container.value( String.format("%s !!!", e.getDescription() ) )
                    );
        
        assertEquals( String.format("%s !!!", expectedError), res.getValue() );
      
    }
    
    @Test
    public void recoverWithKo() {
      
        final String expected = "expected value";
        
        final Container<Error,String> cont = Container.value( expected )  ;      
        
        final Container<Error,String> res =  m.recoverWith( 
                      cont, 
                      e -> Container.value( String.format("%s !!!", e.getDescription() ) )
                    );
        
        assertEquals( expected, res.getValue() );
      
    }
    
    @Test
    public void recoverWithException() {
      
        final String expectedError = "expected error";
        
        final Container<Error,String> cont = Container.error( new MyError( "other error" ) );        
        
        final Container<Error,String> res =  m.recoverWith( 
                      cont, 
                      e -> { 
                          throw new RuntimeException( expectedError ); 
                      }
                    );
        
        assertEquals( expectedError, res.getError().getDescription() );
      
    }
    
    @Test
    public void mapOk() {
      
        final String expected = "one";
        
        final Container<Error,String> cont = Container.value( expected );        
        
        final Container<Error,Integer>  res =  m.map( cont, v -> v.length() );
        
        assertEquals( new Integer( expected.length() ) , res.getValue() ) ; 
      
    }
      
    @Test
    public void mapKo() {
      
        final String expectedError = "expected error";
        
        final Container<Error,String> cont = Container.error( new MyError( expectedError ) );        
        
        final Container<Error,Integer>res =  m.map( cont, v -> v.length()  );
        
        assertEquals( expectedError, res.getError().getDescription() );        
      
    }   
    
    @Test
    public void mapException() {
      
        final String expectedError = "expected error";
        
        final Container<Error,String> cont = Container.value( "one" );       
        
        final Container<Error,Integer> res =  m.map( cont, v -> { 
            throw new RuntimeException( expectedError );
        } );
        
        assertEquals( expectedError, res.getError().getDescription() );        
      
    }
    
    @Test
    public void recoverOk() {
      
        final String expectedError = "expected error";
        
        final Container<Error,String> cont = Container.error( new MyError( expectedError ) );        
        
        final Container<Error,String> res = m.recover( 
                      cont, 
                      e -> String.format("%s !!!", e.getDescription() )
                    );
        
        assertEquals( String.format( "%s !!!", expectedError), res.getValue() );
      
    }
    
    @Test
    public void recoverKo() {
      
        final String expected = "expected value";
        
        final Container<Error,String> cont = Container.value( expected ) ;       
        
        final Container<Error,String> res =  m.recover( 
                      cont, 
                      e -> String.format("%s !!!", e.getDescription() ) 
                    );
        
        assertEquals( expected, res.getValue() );
      
    }
    
    @Test
    public void recoverException() {
      
        final String expectedError = "expected error";
        
        final Container<Error,String> cont = Container.error( new MyError( "other error" ) );        
        
        final Container<Error,String> res = m.recover( 
                      cont, 
                      e -> { 
                          throw new RuntimeException( expectedError ); 
                      }
                    );
        
        assertEquals( expectedError, res.getError().getDescription() );
      
    }
    
    @Test
    public void flattenOk() {
      
        String value = "test";
      
        Container<Error, String> res = m.flatten( m.pure( m.pure( value ) ) );
        
        assertEquals( value, res.getValue() );
      
    }
    
    @Test
    public void flatMap2Ok() {
    
        final Container<Error,String> one = m.pure( "one" );
        final Container<Error, String> two = m.pure( "two" );
        
        final Container<Error, String>  res = m.flatMap2(
          one,
          two,
          ( o, t ) -> m.pure( String.format( "%s, %s", o, t )  )
        );
        
        assertEquals( "one, two" , res.getValue() );
      
    }
    
    @Test
    public void map2Ok() {
    
        final Container<Error,String> one = m.pure( "one" );
        final Container<Error, String> two = m.pure( "two" );
        
        final Container<Error, String> res = m.map2(
          one,
          two,
          ( o, t ) ->  String.format( "%s, %s", o, t )
        
        );
        
        assertEquals( "one, two" , res.getValue() );
      
    }    
    
    @Test
    public void flatMap3Ok() {
    
        final Container<Error,String> one = m.pure( "one" );
        final Container<Error, String> two = m.pure( "two" );
        final Container<Error, String> three = m.pure( "three" );
        
        final Container<Error, String>  res = m.flatMap3(
          one,
          two,
          three,
          ( o, t, tt ) -> m.pure( String.format( "%s, %s, %s", o, t, tt )  )
        );
        
        assertEquals( "one, two, three" , res.getValue() );
      
    } 
    
    @Test
    public void map3Ok() {
    
        final Container<Error,String> one = m.pure( "one" );
        final Container<Error, String> two = m.pure( "two" );
        final Container<Error, String> three = m.pure( "three" );
        
        final Container<Error, String> res = m.map3(
          one,
          two,
          three,
          ( o, t, tt ) ->  String.format( "%s, %s, %s", o, t, tt ) 
        );
        
        assertEquals( "one, two, three" , res.getValue() );
      
    }
    
    @Test
    public void sequenceOk() {
    
        final List<Container<Error, String>> list = Arrays.asList( m.pure( "one" ), m.pure( "two" ) );
        
        Container<Error, List<String>> res = m.sequence( list  );
        
        assertEquals( Arrays.asList( "one", "two" ) , res.getValue() );
      
    }
 

}
